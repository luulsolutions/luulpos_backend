package com.luulsolutions.luulpos.cucumber.stepdefs;

import com.luulsolutions.luulpos.LuulposApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = LuulposApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
