package org.springframework.samples.petclinic.sfg;

import org.springframework.stereotype.Component;

@Component
public class YannyProducer implements WordProducer {
    @Override
    public String getWord() {
        return "Yanny";
    }
}
