package com.noseparte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

/**
 * @Auther: Noseparte
 * @Date: 2019/9/27 16:58
 * @Description:
 *
 *          <p>Spring Cloud Eureka Server 注册中心</p>
 */

@EnableEurekaServer
@SpringBootApplication
@EnableTurbine
@EnableDiscoveryClient
@EnableHystrixDashboard
public class FamousCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(FamousCloudApplication.class, args);
    }

}
