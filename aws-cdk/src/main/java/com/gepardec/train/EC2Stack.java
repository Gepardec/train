package com.gepardec.train;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.CfnRouteTable;
import software.amazon.awscdk.services.ec2.Instance;
import software.amazon.awscdk.services.ec2.InstanceClass;
import software.amazon.awscdk.services.ec2.InstanceSize;
import software.amazon.awscdk.services.ec2.InstanceType;
import software.amazon.awscdk.services.ec2.IpAddresses;
import software.amazon.awscdk.services.ec2.MachineImage;
import software.amazon.awscdk.services.ec2.SubnetConfiguration;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.amazon.awscdk.services.ec2.Vpc;
import software.constructs.Construct;

public class EC2Stack extends Stack {

    public EC2Stack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public EC2Stack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // Configurations by arguments
        var config = new Configuration("container-training",
                1,
                "ubuntu/images/hvm-ssd/ubuntu-focal-20.04-amd64-server-20211129",
                LocalDateTime.now());

        var subnetConfigs = IntStream.range(0, config.instanceCount())
                .mapToObj(idx -> createSubnetConfigurationForInstance(config, idx))
                .collect(Collectors.toList());
        var vpc = createVpcForSubnets(subnetConfigs, config);
        var route = createRouteForVpc(vpc, config);
        var ec2Instances = IntStream.range(0, config.instanceCount())
                .mapToObj(idx -> createEc2Instances(config, vpc, idx))
                .collect(Collectors.toList());
    }

    SubnetConfiguration createSubnetConfigurationForInstance(Configuration config, int count) {
        return SubnetConfiguration.builder()
                .name(config.training() + "-subnet-" + count)
                .cidrMask(24)
                .subnetType(SubnetType.PUBLIC)
                .mapPublicIpOnLaunch(true)
                .build();
    }

    Vpc createVpcForSubnets(List<SubnetConfiguration> subnets, Configuration config) {
        return Vpc.Builder.create(this, config.training() + "-vpc-subnet")
                .ipAddresses(IpAddresses.cidr("10.0.0.0/16"))
                .enableDnsHostnames(true)
                .enableDnsSupport(true)
                .subnetConfiguration(subnets)
                .maxAzs(1)
                .build();
    }

    CfnRouteTable createRouteForVpc(Vpc vpc, Configuration config) {
        return CfnRouteTable.Builder.create(this, config.training() + "-route-table")
                .vpcId(vpc.getVpcId())
                .build();
    }

    Instance createEc2Instances(Configuration config, Vpc vpc, int count) {
        var machineImage = MachineImage.genericLinux(Map.of(getRegion(), config.amiName()));

        return Instance.Builder.create(this, config.training() + "-ec2-" + count)
                .instanceName(config.training() + "-ec2-instance-" + count)
                .machineImage(machineImage)
                .instanceType(InstanceType.of(InstanceClass.T2, InstanceSize.MEDIUM))
                .vpc(vpc)
                .build();
    }
}
