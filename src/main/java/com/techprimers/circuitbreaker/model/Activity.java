package com.techprimers.circuitbreaker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* sample message
    {
        "activity": "Start a family tree",
            "type": "social",
            "participants": 1,
            "price": 0,
            "link": "https://en.wikipedia.org/wiki/Family_tree",
            "key": "6825484",
            "accessibility": 1
    }*/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Activity {
   private int id;
   private  String advice;
}
