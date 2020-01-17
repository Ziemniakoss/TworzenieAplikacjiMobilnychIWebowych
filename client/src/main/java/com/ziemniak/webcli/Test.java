package com.ziemniak.webcli;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Test {
	public static void main(String[] args) {
		new Test().start();
	}

	public void start() {
		Hek[] heks = {
				new Hek("aa"),
				new Hek("1"),
				new Hek("ddd"),
				new Hek("aaa")
		};
		List<String> a = Arrays.stream(heks).map(Hek::getS).collect(Collectors.toList());
		System.out.println(String.join(" ",a));
	}

	class Hek {
		String s;

		public Hek(String s) {
			this.s = s;
		}

		public String getS() {
			return s;
		}

		public void setS(String s) {
			this.s = s;
		}
	}
}
