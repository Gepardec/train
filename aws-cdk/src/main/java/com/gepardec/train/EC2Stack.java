package com.gepardec.train;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.iam.Role;
import software.constructs.Construct;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EC2Stack extends Stack {

    private final Configuration config;

    public EC2Stack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public EC2Stack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        try {
            // Configurations by arguments
            config = new Configuration("containerTraining",
                    2,
                    "ami-06b4d9ba1f23a8da4",
                    LocalDateTime.now());

            var subnetConfigs = IntStream.range(0, config.instanceCount()).mapToObj(idx -> createSubnetConfigurationForInstance(config, idx)).collect(Collectors.toList());
            var keyPairs = IntStream.range(0, config.instanceCount()).mapToObj(i -> createKeyPairForInstance(i)).collect(Collectors.toList());
            var vpc = createVpcForSubnets(subnetConfigs, config);
            var securityGroup = createSecurityGroup(vpc);
            var ec2Instances = IntStream.range(0, config.instanceCount()).mapToObj(idx -> createEc2Instances(config, vpc, securityGroup, keyPairs.get(idx), idx)).collect(Collectors.toList());

            for (int i = 0; i < config.instanceCount(); i++) {
                var instance = ec2Instances.get(i);
                CfnOutput.Builder.create(this, config.indexedIdSuffix("EC2 Public IP", i)).value(instance.getInstancePublicIp()).build();
                CfnOutput.Builder.create(this, config.indexedIdSuffix("EC2 Public DNS", i)).value(instance.getInstancePublicDnsName()).build();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private SecurityGroup createSecurityGroup(Vpc vpc) {
        var securityGroup = SecurityGroup.Builder.create(this, config.idSuffix("SecurityGroup"))
                .vpc(vpc)
                .build();
        securityGroup.addIngressRule(Peer.anyIpv4(), Port.tcp(22), "Allow ssh access");
        securityGroup.addIngressRule(Peer.anyIpv4(), Port.tcp(80), "Allow http access");
        securityGroup.addIngressRule(Peer.anyIpv4(), Port.tcp(443), "Allow https access");
        securityGroup.addIngressRule(Peer.anyIpv4(), Port.tcp(9080), "Allow non-privileged http access");
        securityGroup.addIngressRule(Peer.anyIpv4(), Port.tcp(9443), "Allow non-privileged https access");
        return securityGroup;
    }

    private CfnKeyPair createKeyPairForInstance(int idx) {
        return CfnKeyPair.Builder.create(this, config.indexedIdSuffix("KeyPair", idx))
                .keyName(config.indexedIdSuffix("KeyPair", idx))
                .build();
    }

    SubnetConfiguration createSubnetConfigurationForInstance(Configuration config, int idx) {
        return SubnetConfiguration.builder()
                .name(config.indexedIdSuffix("Subnet", idx))
                .cidrMask(24)
                .subnetType(SubnetType.PUBLIC)
                .mapPublicIpOnLaunch(true)
                .build();
    }

    Vpc createVpcForSubnets(List<SubnetConfiguration> subnets, Configuration config) {
        return Vpc.Builder.create(this, config.idSuffix("VPC"))
                .ipAddresses(IpAddresses.cidr("10.0.0.0/16"))
                .enableDnsHostnames(true)
                .enableDnsSupport(true)
                .subnetConfiguration(subnets)
                .maxAzs(1)
                .build();
    }

    CfnRouteTable createRouteForVpc(Vpc vpc, Configuration config) {
        return CfnRouteTable.Builder.create(this, config.training() + "RoutTable")
                .vpcId(vpc.getVpcId())
                .build();
    }

    Instance createEc2Instances(Configuration config, Vpc vpc, SecurityGroup securityGroup, CfnKeyPair keyPair, int idx) {
        var machineImage = MachineImage.genericLinux(Map.of(getRegion(), config.amiName()));

        return Instance.Builder.create(this, config.indexedIdSuffix("EC2Instance", idx))
                .instanceName(config.indexedIdSuffix("EC2Instance", idx))
                .machineImage(machineImage)
                .instanceType(InstanceType.of(InstanceClass.T2, InstanceSize.MEDIUM))
                .vpc(vpc)
                .keyName(keyPair.getKeyName())
                .securityGroup(securityGroup)
                .allowAllOutbound(true)
                .build();
    }
}
