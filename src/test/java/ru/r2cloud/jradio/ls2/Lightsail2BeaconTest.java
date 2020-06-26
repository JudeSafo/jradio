package ru.r2cloud.jradio.ls2;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

import org.junit.Test;

import pl.pojo.tester.api.assertion.Method;
import ru.r2cloud.jradio.AssertJson;
import ru.r2cloud.jradio.fec.ViterbiTest;
import ru.r2cloud.jradio.ip.Header;

public class Lightsail2BeaconTest {

	@Test
	public void testBeacon() throws Exception {
		byte[] data = ViterbiTest.hexStringToByteArray(
				"9C6C86A040400296966C9092A81503CC45000100000040000111848781419323E0000001C350000200EC715E01C7C7C7CAC2C5C4C5C5C431842A6A036A04A2000000000084FF84FF84FF8400840084FF840084008400000AF800005DFC0003F137000004710001821000000000000028C4000AF3480000002601954B3D461D4B3D3AF5000B0000001800000000000028F00684C5F0000584C4F0000784C4F0000584C4F000EE85C4F0000F85C4F000E985C4B001E985C5B001FD29000100250035FFD70066FFFFFFFF0001000000000005FFFFDD00240025002600160025001B001500140014002700000000AB091EECB500E502FFEB6E00002EC58C202EC58C20FFFFFFFF000000000000000002118B");
		Lightsail2Beacon result = new Lightsail2Beacon();
		result.readBeacon(data);
		AssertJson.assertObjectsEqual("Lightsail2Beacon.json", result);
	}

	@Test
	public void testPojo() {
		assertPojoMethodsFor(Lightsail2Beacon.class).testing(Method.GETTER, Method.SETTER).areWellImplemented();
		assertPojoMethodsFor(Header.class).testing(Method.GETTER, Method.SETTER).areWellImplemented();
		assertPojoMethodsFor(ru.r2cloud.jradio.udp.Header.class).testing(Method.GETTER, Method.SETTER).areWellImplemented();
		assertPojoMethodsFor(BeaconData.class).testing(Method.GETTER, Method.SETTER).areWellImplemented();
		assertPojoMethodsFor(SysmgrData.class).testing(Method.GETTER, Method.SETTER).areWellImplemented();
		assertPojoMethodsFor(CommData.class).testing(Method.GETTER, Method.SETTER).areWellImplemented();
		assertPojoMethodsFor(BatteryData.class).testing(Method.GETTER, Method.SETTER).areWellImplemented();
		assertPojoMethodsFor(CameraInfo.class).testing(Method.GETTER, Method.SETTER).areWellImplemented();
	}

}
