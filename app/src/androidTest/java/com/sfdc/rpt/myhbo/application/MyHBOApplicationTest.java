package com.sfdc.rpt.myhbo.application;

import android.support.test.runner.AndroidJUnit4;
import android.test.ApplicationTestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * MyHMBOApplication class test
 *
 * Created by ahetawal on 3/13/16.
 */
@RunWith(AndroidJUnit4.class)
public class MyHBOApplicationTest extends ApplicationTestCase<MyHBOApplication> {

    private MyHBOApplication application;

    public MyHBOApplicationTest() {
        super(MyHBOApplication.class);
    }


    @Override
    public void setUp() throws Exception {
        super.setUp();
        createApplication();
        application = getApplication();

    }

    /**
     * Test for verifying if the application is started up correctly.<br/>
     * Verify if the custom intent action value matches to "com.sfdc.rpt.myhbo.DETAILVIEW"
     */
    @Test
    public void testIntentConstant(){
        String intentString = ((MyHBOApplication)application).VIEW_DETAIL_ACTION;
        assertEquals("com.sfdc.rpt.myhbo.DETAILVIEW", intentString);
    }

}
