package com.gc.irc.common.utils.encoder.recursive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.gc.irc.common.utils.encoder.recursive.test.entities.ObjectToEncode;
import com.gc.irc.common.utils.encoder.recursive.test.entities.SpecificObjectToEncodeEncoder;


public class ReflectionEncoderTest {

    /**
     * Max deep check.
     */
    @Test
    public void maxDeepCheck() {
        ReflectionEncoder encoder = new ReflectionEncoderImpl(new AbstractStringEncoder() {

            @Override
            public String encode(String value) {
                return value;
            }
        });

        encoder.setMaxDeep(-5);
        assertEquals(ReflectionEncoderImpl.DEFAULT_MAX_DEEP, ((ReflectionEncoderImpl) encoder).getMaxDeep());
        encoder.setMaxDeep(12);
        assertEquals(12, ((ReflectionEncoderImpl) encoder).getMaxDeep());
    }

    /**
     * Coll map.
     */
    @Test
    public void collMap() {
        ReflectionEncoder encoder = new ReflectionEncoderImpl(new AbstractStringEncoder() {

            @Override
            public String encode(String value) {
                return "#$#" + value;
            }
        });
        List < Object > list1 = new ArrayList < Object >();
        Map < String, Object > map1 = new HashMap < String, Object >();
        map1.put("key1", "value1");
        list1.add(map1);
        Object[] tab1 = Arrays.asList("value2", map1, list1).toArray();
        list1.add(tab1);
        map1.put("tab1", tab1);
        List < String > list2 = new ArrayList < String >(Arrays.asList("value3", "value4"));
        list1.add(list2);
        map1.put("list2", list2);

        assertEquals("value1", map1.get("key1"));
        assertEquals("value2", tab1[0]);
        assertEquals("value3", list2.get(0));
        assertEquals("value4", list2.get(1));

        encoder.encodeByReflection(list1);

        assertEquals("#$#value1", map1.get("key1"));
        assertEquals("#$#value2", tab1[0]);
        assertEquals("#$#value3", list2.get(0));
        assertEquals("#$#value4", list2.get(1));
    }

    /**
     * Interface black list.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void interfaceBlackList() {
        ReflectionEncoder encoder = new ReflectionEncoderImpl(new AbstractStringEncoder() {
            @Override
            public String encode(String value) {
                return "#$#" + value;
            }
        });
        encoder.addInterfacesToBlackList(new HashSet < Class >(Arrays.asList(Map.class)));
        List < Object > list1 = new ArrayList < Object >();
        Map < String, Object > map1 = new HashMap < String, Object >();
        map1.put("key1", "value1");
        list1.add(map1);
        Object[] tab1 = Arrays.asList("value2", map1, list1).toArray();
        list1.add(tab1);
        List < String > list2 = new ArrayList < String >(Arrays.asList("value3", "value4"));
        list1.add(list2);
        map1.put("key2", "value5");

        assertEquals("value1", map1.get("key1"));
        assertEquals("value2", tab1[0]);
        assertEquals("value3", list2.get(0));
        assertEquals("value4", list2.get(1));
        assertEquals("value5", map1.get("key2"));

        encoder.encodeByReflection(list1);

        assertEquals("value1", map1.get("key1"));
        assertEquals("#$#value2", tab1[0]);
        assertEquals("#$#value3", list2.get(0));
        assertEquals("#$#value4", list2.get(1));
        assertEquals("value5", map1.get("key2"));
    }

    /**
     * Max deep check.
     */
    @Test
    public void Deep() {
        ReflectionEncoder encoder = new ReflectionEncoderImpl(new AbstractStringEncoder() {

            @Override
            public String encode(String value) {
                return "#$#" + value;
            }
        });
        encoder.setMaxDeep(6);
        assertEquals(6, ((ReflectionEncoderImpl) encoder).getMaxDeep());
        Object[] tab1 = new Object[2];
        tab1[0] = "value1";
        Object[] tab2 = new Object[2];
        tab2[0] = "value2";
        tab1[1] = tab2;
        Object[] tab3 = new Object[2];
        tab3[0] = "value3";
        tab2[1] = tab3;
        Object[] tab4 = new Object[2];
        tab4[0] = "value4";
        tab3[1] = tab4;
        Object[] tab5 = new Object[2];
        tab5[0] = "value5";
        tab4[1] = tab5;
        Object[] tab6 = new Object[2];
        tab6[0] = "value6";
        tab5[1] = tab6;
        Object[] tab7 = new Object[2];
        tab7[0] = "value7";
        tab6[1] = tab7;
        Object[] tab8 = new Object[1];
        tab8[0] = "value8";
        tab7[1] = tab8;

        assertEquals("value1", tab1[0]);
        assertEquals("value2", tab2[0]);
        assertEquals("value3", tab3[0]);
        assertEquals("value4", tab4[0]);
        assertEquals("value5", tab5[0]);
        assertEquals("value6", tab6[0]);
        assertEquals("value7", tab7[0]);
        assertEquals("value8", tab8[0]);

        encoder.encodeByReflection(tab1);

        assertEquals("#$#value1", tab1[0]);
        assertEquals("#$#value2", tab2[0]);
        assertEquals("#$#value3", tab3[0]);
        assertEquals("#$#value4", tab4[0]);
        assertEquals("#$#value5", tab5[0]);
        assertEquals("#$#value6", tab6[0]);
        assertEquals("value7", tab7[0]);
        assertEquals("value8", tab8[0]);
    }

    @Test
    public void doubleInheranceObjectEncoder() {
        ReflectionEncoder encoder = new ReflectionEncoderImpl(new StringEncoder() {

            @Override
            public String encode(String value) {
                return "#$#" + value;
            }
        }, Arrays.asList((ObjectEncoder) new SpecificObjectToEncodeEncoder()));

        ObjectToEncode obj1 = new ObjectToEncode("a", "z", "e");
        ObjectToEncode obj2 = new ObjectToEncode("q", "s", "d");
        assertEquals("a", obj1.getParam1());
        assertEquals("z", obj1.getParam2());
        assertEquals("e", obj1.getParam3());
        assertEquals("q", obj2.getParam1());
        assertEquals("s", obj2.getParam2());
        assertEquals("d", obj2.getParam3());
        List < ObjectToEncode > list = new ArrayList < ObjectToEncode >();
        list.add(obj1);
        list.add(obj2);

        encoder.encodeByReflection(list);

        assertEquals("#$#a", obj1.getParam1());
        assertEquals("#$#z", obj1.getParam2());
        assertEquals("e", obj1.getParam3());
        assertEquals("#$#q", obj2.getParam1());
        assertEquals("#$#s", obj2.getParam2());
        assertEquals("d", obj2.getParam3());

    }

}
