package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitWebConfig(locations = {"classpath:spring/mvc-test-config.xml","classpath:spring/mvc-core-config.xml"})
class OwnerControllerTest {

    @Autowired
    OwnerController controller;

    @Autowired
    ClinicService clinicService;

    @Test
    void tempTest() {
    }
}
