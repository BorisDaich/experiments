package biz.daich.experiments.boot.metrics.test;

import java.io.Serializable;

import lombok.Data;

@Data
public class DataObj implements Serializable
{
	String	type;
	String	name;
}
