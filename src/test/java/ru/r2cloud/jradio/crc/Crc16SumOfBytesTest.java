package ru.r2cloud.jradio.crc;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class Crc16SumOfBytesTest {

	@Test
	public void success() {
		byte[] data = "123456789".getBytes(StandardCharsets.ISO_8859_1);
		assertEquals(0x01DD, Crc16SumOfBytes.calculate(data));
	}
}
