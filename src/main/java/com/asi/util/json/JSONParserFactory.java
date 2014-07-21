package com.asi.util.json;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;

public class JSONParserFactory implements IParserFactory {

	private JSONParser parser = null;
	private ContainerFactory containerFactory = null;

	@Override
	public void creatParser() {
		parser = new JSONParser();
	}

	@Override
	public void createContainerFatory() {

		containerFactory = new ContainerFactory() {
			public List<?> creatArrayContainer() {
				return new LinkedList<Object>();
			}

			public Map<?, ?> createObjectContainer() {
				return new LinkedHashMap<Object, Object>();
			}
		};

	}

	@Override
	public Object getContainerFatory() {

		return containerFactory;
	}

	@Override
	public Object getParser() {

		return parser;
	}

}
