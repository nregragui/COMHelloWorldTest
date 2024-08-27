package com.mjog.hello.controller;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;
import io.gatling.javaapi.http.HttpProtocolBuilder;

public class HelloWorldControllerLoadTest extends Simulation {

    public void loadTest() {
        HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080") // Base URL
            .acceptHeader("application/json") // Common headers
            .contentTypeHeader("application/json");

        ScenarioBuilder scn = scenario("Load Test")
            .exec(
                http("Hello Request")
                    .post("/hello")
                    .body(StringBody("{\"message\": \"test\"}")).asJson()
                    .check(status().is(200))
            );

        setUp(
            scn.injectOpen(
                constantUsersPerSec(1000).during(60) // Alternative to heavisideUsers
            ).protocols(httpProtocol)
        ).maxDuration(120); // Maximum duration of the test
    }
}