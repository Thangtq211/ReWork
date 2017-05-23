package com.tmobile.federation.services;

import com.tmobile.federation.beans.req.FedIdRequestBean;
import com.tmobile.federation.beans.req.userProfile.UserProfiles;
import com.tmobile.federation.beans.res.GlobalErrorBean;
import com.tmobile.federation.beans.res.GlobalResponseBean;
import com.tmobile.federation.dao.IAMUserDAO;
import com.tmobile.federation.dao.IAMUserDAOImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

import java.util.Arrays;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author ThangTQ
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestContext.class })
public class CreateFedIdServiceTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CreateFedIdservice createFedIdservice;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
          .standaloneSetup(createFedIdservice)
          .build();
    }

    @Test
    public void testGetFedIDService() throws Exception {
        IAMUserDAO iamUserDAO = new IAMUserDAOImpl();
        createFedIdservice.setIAMUserDAO(iamUserDAO);
        ResultActions resultActions = mockMvc
          .perform(get("/provisioning/v1/fedId?iamUserId=U-82bf6ba6-88f5-4049-b86c-860dca0c9fb3&email=email@mobile.com&tenantId=xyz123")
            .contentType("application/json")
            .header("authorization", "admin:123123")
            .header("transactionId", "123123")
          )
          .andExpect(status().isOk());
        System.out.println("passed xxxx");
        resultActions.andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void queryFederatedIDFromWebLogic() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("authorization", "admin:123123");
        headers.add("transactionId", "123123");
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String queryFederatedID = "http://10.30.164.132:7001/authenticateb/provisioning/v1/fedId?iamUserId=U-82bf6ba6-88f5-4049-b86c-860dca0c9fb3&email=email@mobile.com&tenantId=xyz123";
        ResponseEntity<String> responseEntity = restTemplate.exchange(queryFederatedID, HttpMethod.GET, entity, String.class);

        String responseBody = responseEntity.getBody();
        System.out.println("responseEntity UserProfiles from Server .... \n" + responseBody);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(responseBody,containsString("role"));
        assertThat(responseBody,
          hasJsonPath("role", containsString("SuperAdmin")));
    }



}
