package org.opennms.netmgt.rrd.newts;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.test.OpenNMSJUnit4ClassRunner;
import org.opennms.netmgt.rrd.RrdDataSource;
import org.opennms.newts.api.Sample;
import org.opennms.test.JUnitConfigurationEnvironment;
import org.springframework.test.context.ContextConfiguration;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@RunWith(OpenNMSJUnit4ClassRunner.class)
@ContextConfiguration(locations={
        "classpath:/META-INF/opennms/applicationContext-empty.xml",
})
@JUnitConfigurationEnvironment
public class NewtsUtilsTest {

    private String rrdBaseDir;

    @Before
    public void setUp() {
        rrdBaseDir = System.getProperty("rrd.base.dir");
    }

    @Test
    public void addParentPathAttributes() {
        Map<String, String> attributes = Maps.newHashMap();
        String path = rrdBaseDir + "/snmp/1/loadavg1" + NewtsRrdStrategy.FILE_EXTENSION;
        NewtsUtils.addParentPathAttributes(path, attributes);

        assertEquals("snmp", attributes.get("_parent0"));
        assertEquals("snmp:1", attributes.get("_parent1"));
        assertEquals(2, attributes.size());
    }

    @Test
    public void canConvertFilenameToResource() {
        String resourceId = NewtsUtils.getResourceIdFromPath(rrdBaseDir + "/snmp/1/loadavg1.newts");
        assertEquals("snmp:1:loadavg1", resourceId);

        // What if there's already a ':' in the filename?
        resourceId = NewtsUtils.getResourceIdFromPath(rrdBaseDir + "/snmp/1/load:avg1.newts");
        assertEquals("snmp:1:loadavg1", resourceId);

        resourceId = NewtsUtils.getResourceIdFromPath(rrdBaseDir + "/snmp/1/eth0-04013f75f101/ifInOctets.newts");
        assertEquals("snmp:1:eth0-04013f75f101:ifInOctets", resourceId);
    }

    @Test
    public void canConvertRrdUpdateStringToSampleSet() {
        RrdDataSource ds1 = new RrdDataSource("x", "GAUGE", 900, "0", "100");
        RrdDataSource ds2 = new RrdDataSource("y", "GAUGE", 900, "0", "100");
        RrdDef def = new RrdDef(rrdBaseDir + "/snmp/1", "loadavg1", Lists.newArrayList(ds1, ds2));
        List<Sample> samples = NewtsUtils.getSamplesFromRrdUpdateString(def, "1:U:9", null);
        assertEquals(2, samples.size());
    }
}