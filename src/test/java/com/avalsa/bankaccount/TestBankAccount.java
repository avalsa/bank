package com.avalsa.bankaccount;

import com.avalsa.bankaccount.model.BankAccount;
import com.avalsa.bankaccount.repository.BankAccountRepository;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TestBankAccount {
    private String BASE_URL;

    @LocalServerPort
    private int port;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Before
    public void setUp() {
        BASE_URL = "http://localhost:" + port + "/bankaccount/";
    }

    @Test
    public void testCreate() throws Exception {
        //validation
        createBankAccount("abc", SC_BAD_REQUEST);
        createBankAccount("1234", SC_BAD_REQUEST);
        createBankAccount("123456", SC_BAD_REQUEST);
        //successful create
        createBankAccount("12345", SC_OK);
        createBankAccount("54321", SC_OK);
        //duplicate
        createBankAccount("12345", SC_BAD_REQUEST);
        checkBalance("12345", 0);
        checkBalance("54321", 0);
    }

    @Test
    public void testWithdraw() throws Exception {
        //validation
        withdrawBankAccount("abc", "abc", SC_BAD_REQUEST, null);
        withdrawBankAccount("12345", "", SC_BAD_REQUEST, null);
        withdrawBankAccount("12345", "abc", SC_BAD_REQUEST, null);
        withdrawBankAccount("abc", "12345", SC_BAD_REQUEST, null);
        withdrawBankAccount("12345", "-12", SC_BAD_REQUEST, null);
        withdrawBankAccount("12345", "12", SC_NOT_FOUND, null);
        //create ba
        createBankAccount("12345", SC_OK);
        //insufficient funds
        withdrawBankAccount("12345", "12", SC_BAD_REQUEST, null);
        //deposit
        depositBankAccount("12345", "12", SC_OK, 12);
        //withdraws
        withdrawBankAccount("12345", "10", SC_OK, 2);
        withdrawBankAccount("12345", "2", SC_OK, 0);
        withdrawBankAccount("12345", "1", SC_BAD_REQUEST, 0);
    }

    @Test
    public void testDeposit() throws Exception {
        //validation
        depositBankAccount("abc", "abc", SC_BAD_REQUEST, null);
        depositBankAccount("12345", "", SC_BAD_REQUEST, null);
        depositBankAccount("12345", "abc", SC_BAD_REQUEST, null);
        depositBankAccount("abc", "12345", SC_BAD_REQUEST, null);
        depositBankAccount("12345", "-12", SC_BAD_REQUEST, null);
        depositBankAccount("12345", "12", SC_NOT_FOUND, null);
        //create ba
        createBankAccount("12345", SC_OK);
        //deposit
        depositBankAccount("12345", "12", SC_OK, 12);
        depositBankAccount("12345", "10", SC_OK, 22);
    }

    @Test
    public void testBalance() throws Exception {
        //validation
        balanceBankAccount("abc", SC_BAD_REQUEST, null);
        balanceBankAccount("1234", SC_BAD_REQUEST, null);
        balanceBankAccount("123456", SC_BAD_REQUEST, null);
        balanceBankAccount("12345", SC_NOT_FOUND, null);
        //create ba
        createBankAccount("12345", SC_OK);
        balanceBankAccount("12345", SC_OK, 0);
        //deposit and then balance
        depositBankAccount("12345", "12", SC_OK, 12);
        balanceBankAccount("12345", SC_OK, 12);
        //withdraw and then balance
        withdrawBankAccount("12345", "5", SC_OK, 7);
        balanceBankAccount("12345", SC_OK, 7);
    }

    private void createBankAccount(String id, Integer httpStatusExpected) throws IOException {
        HttpResponse httpResponse = Request.Post(BASE_URL + id)
                .execute().returnResponse();
        checkHttpStatus(httpResponse, httpStatusExpected);
    }

    private void withdrawBankAccount(String id, String amount, Integer httpStatusExpected, Integer balanceExpected) throws IOException {
        HttpResponse httpResponse = Request.Put(BASE_URL + id + "/withdraw").bodyString(amount, ContentType.APPLICATION_JSON).execute().returnResponse();
        checkHttpStatus(httpResponse, httpStatusExpected);
        checkBalance(id, balanceExpected);
    }

    private void depositBankAccount(String id, String amount, Integer httpStatusExpected, Integer balanceExpected) throws IOException {
        HttpResponse httpResponse = Request.Put(BASE_URL + id + "/deposit").bodyString(amount, ContentType.APPLICATION_JSON).execute().returnResponse();
        checkHttpStatus(httpResponse, httpStatusExpected);
        checkBalance(id, balanceExpected);
    }

    private void balanceBankAccount(String id, Integer httpStatusExpected, Integer balanceExpected) throws IOException {
        HttpResponse httpResponse = Request.Get(BASE_URL + id + "/balance").execute().returnResponse();
        checkHttpStatus(httpResponse, httpStatusExpected);
        if (balanceExpected != null) {
            assertEquals(balanceExpected.intValue(), Integer.parseInt(EntityUtils.toString(httpResponse.getEntity())));
        }
    }

    private static void checkHttpStatus(HttpResponse httpResponse, Integer httpStatusExpected) {
        if (httpStatusExpected != null) {
            assertEquals(httpStatusExpected.intValue(), httpResponse.getStatusLine().getStatusCode());
        }
    }

    private void checkBalance(String id, Integer balanceExpected) {
        if (balanceExpected != null) {
            BankAccount bankAccount = bankAccountRepository.findById(Integer.parseInt(id)).orElseThrow(AssertionError::new);
            assertEquals(balanceExpected, bankAccount.getBalance());
        }
    }
}
