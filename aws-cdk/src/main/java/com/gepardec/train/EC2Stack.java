package com.gepardec.train;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.*;
import software.constructs.Construct;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EC2Stack extends Stack {

    private static final Configuration config = Configuration.CONFIG;

    public EC2Stack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public EC2Stack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        var subnetConfigs = IntStream.range(0, config.instanceCount).mapToObj(idx -> createSubnetConfigurationForInstance(config, idx)).collect(Collectors.toList());
        var keyPairs = IntStream.range(0, config.instanceCount).mapToObj(i -> createKeyPairForInstance(i)).collect(Collectors.toList());
        var vpc = createVpcForSubnets(subnetConfigs);
        var securityGroup = createSecurityGroup(vpc);
        var ec2Instances = IntStream.range(0, config.instanceCount).mapToObj(idx -> createEc2Instances(config, vpc, securityGroup, keyPairs.get(idx), idx)).collect(Collectors.toList());

        for (int i = 0; i < config.instanceCount; i++) {
            var instance = ec2Instances.get(i);
            CfnOutput.Builder.create(this, "EC2 Public IP of Instance " + i).value(instance.getInstancePublicIp()).build();
            CfnOutput.Builder.create(this, "EC2 Public DNS of Instance " + i).value(instance.getInstancePublicDnsName()).build();
        }
    }

    private SecurityGroup createSecurityGroup(Vpc vpc) {
        var securityGroup = SecurityGroup.Builder.create(this, config.idSuffix("SecurityGroup"))
                .vpc(vpc)
                .build();
        securityGroup.addIngressRule(Peer.anyIpv4(), Port.allTraffic(), "Allow all outbound");
        securityGroup.addEgressRule(Peer.anyIpv4(), Port.allTraffic(), "Allow all inbound");
        return securityGroup;
    }

    private CfnKeyPair createKeyPairForInstance(int idx) {
        return CfnKeyPair.Builder.create(this, config.indexedIdSuffix("KeyPair", idx))
                .keyName(config.indexedIdSuffix("KeyPair", idx))
                .publicKeyMaterial(config.indexedPublicKey(idx))
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

    Vpc createVpcForSubnets(List<SubnetConfiguration> subnets) {
        return Vpc.Builder.create(this, config.idSuffix("VPC"))
                .ipAddresses(IpAddresses.cidr("10.0.0.0/16"))
                .enableDnsHostnames(true)
                .enableDnsSupport(true)
                .subnetConfiguration(subnets)
                .maxAzs(1)
                .build();
    }

    Instance createEc2Instances(Configuration config, Vpc vpc, SecurityGroup securityGroup, CfnKeyPair keyPair, int idx) {
        var instance = Instance.Builder.create(this, config.indexedIdSuffix("EC2Instance", idx))
                .instanceName(config.indexedIdSuffix("EC2Instance", idx))
                .machineImage(MachineImage.genericLinux(Map.of(getRegion(), config.ami)))
                .instanceType(InstanceType.of(InstanceClass.T2, InstanceSize.MEDIUM))
                .vpc(vpc)
                .keyName(keyPair.getKeyName())
                .securityGroup(securityGroup)
                .build();

        config.bootstrapFile().ifPresent(instance::addUserData);

        return instance;
    }
}
