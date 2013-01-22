package com.acp.vision.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.acp.common.crypto.abs.AbstractStringEncoder;
import com.acp.common.crypto.api.IObjectEncoder;
import com.acp.common.crypto.api.IStringEncoder;
import com.acp.common.crypto.exception.EncoderException;
import com.acp.provider.message.aggregate.Card;
import com.acp.provider.message.aggregate.CardInfo;
import com.acp.provider.message.aggregate.ContractHolder;
import com.acp.provider.message.crypto.encrypter.CardInfoEncrypter;
import com.acp.provider.message.crypto.encrypter.ContractHolderEncrypter;
import com.acp.vision.encoder.AbstractObjectEncoder;
import com.acp.vision.encoder.IReflectionEncoder;
import com.acp.vision.encoder.impl.ReflectionEncoder;
import com.acp.vision.utils.test.entities.ObjectToEncode;
import com.acp.vision.utils.test.entities.SpecificObjectToEncodeEncoder;

public class ReflectionEncoderTest {

    @Test
    public void simpleEncoding() {
        IReflectionEncoder encoder = new ReflectionEncoder(new IStringEncoder() {

            @Override
            public String encode(String value) {
                return "encrypt####" + value;
            }
        }, Arrays.asList((IObjectEncoder) new CardInfoEncrypter(), (IObjectEncoder) new ContractHolderEncrypter()));

        Card card = new Card();
        CardInfo cardInfo = new CardInfo();
        cardInfo.setCardNumber("1111222233334444");
        cardInfo.setBrandDesc("test");
        card.setCardInfo(cardInfo);

        ContractHolder contractHolder1 = new ContractHolder();
        contractHolder1.setContractHolderId("1");
        contractHolder1.setContractHolderType("type1");

        ContractHolder contractHolder2 = new ContractHolder();
        contractHolder2.setContractHolderId("2");
        contractHolder2.setContractHolderType("type2");
        card.setContractHolder((ContractHolder[]) Arrays.asList(contractHolder1, contractHolder2).toArray());

        encoder.encodeByReflection(card);

        assertNotNull(card);
        assertEquals("test", cardInfo.getBrandDesc());
        assertEquals("encrypt####1111222233334444", cardInfo.getCardNumber());
        assertEquals("encrypt####1", contractHolder1.getContractHolderId());
        assertEquals("type1", contractHolder1.getContractHolderType());
        assertEquals("encrypt####2", contractHolder2.getContractHolderId());
        assertEquals("type2", contractHolder2.getContractHolderType());
    }

    @Test
    public void stringEncoding() {
        IReflectionEncoder encoder = new ReflectionEncoder(new AbstractStringEncoder() {

            @Override
            public String encode(String value) {
                return "Encode###" + value;
            }
        });

        Card card = new Card();
        CardInfo cardInfo = new CardInfo();
        cardInfo.setCardNumber("1111222233334444");
        cardInfo.setBrandDesc("test");
        card.setCardInfo(cardInfo);

        ContractHolder contractHolder1 = new ContractHolder();
        contractHolder1.setContractHolderId("1");
        contractHolder1.setContractHolderType("type1");

        ContractHolder contractHolder2 = new ContractHolder();
        contractHolder2.setContractHolderId("2");
        contractHolder2.setContractHolderType("type2");
        card.setContractHolder((ContractHolder[]) Arrays.asList(contractHolder1, contractHolder2, contractHolder1).toArray());

        encoder.encodeByReflection(card);

        assertEquals("Encode###test", cardInfo.getBrandDesc());
        assertEquals("Encode###1111222233334444", cardInfo.getCardNumber());
        assertEquals("Encode###1", contractHolder1.getContractHolderId());
        assertEquals("Encode###type1", contractHolder1.getContractHolderType());
        assertEquals("Encode###2", contractHolder2.getContractHolderId());
        assertEquals("Encode###type2", contractHolder2.getContractHolderType());
    }

    /**
     * Max deep check.
     */
    @Test
    public void maxDeepCheck() {
        IReflectionEncoder encoder = new ReflectionEncoder(new AbstractStringEncoder() {

            @Override
            public String encode(String value) {
                return value;
            }
        });

        encoder.setMaxDeep(-5);
        assertEquals(ReflectionEncoder.DEFAULT_MAX_DEEP, ((ReflectionEncoder) encoder).getMaxDeep());
        encoder.setMaxDeep(12);
        assertEquals(12, ((ReflectionEncoder) encoder).getMaxDeep());
    }

    /**
     * Coll map.
     */
    @Test
    public void collMap() {
        IReflectionEncoder encoder = new ReflectionEncoder(new AbstractStringEncoder() {

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
     * Class black list.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void classBlackList() {
        IReflectionEncoder encoder = new ReflectionEncoder(new AbstractStringEncoder() {
            @Override
            public String encode(String value) {
                return "#$#" + value;
            }
        });
        encoder.addToBlackList(new HashSet < Class >(Arrays.asList(ContractHolder.class)));

        Card card = new Card();
        CardInfo cardInfo = new CardInfo();
        cardInfo.setCardNumber("1111222233334444");
        cardInfo.setBrandDesc("test");
        card.setCardInfo(cardInfo);

        ContractHolder contractHolder1 = new ContractHolder();
        contractHolder1.setContractHolderId("1");
        contractHolder1.setContractHolderType("type1");

        ContractHolder contractHolder2 = new ContractHolder();
        contractHolder2.setContractHolderId("2");
        contractHolder2.setContractHolderType("type2");
        card.setContractHolder((ContractHolder[]) Arrays.asList(contractHolder1, contractHolder2, contractHolder1).toArray());

        encoder.encodeByReflection(card);

        assertNotNull(card);
        assertEquals("#$#test", cardInfo.getBrandDesc());
        assertEquals("#$#1111222233334444", cardInfo.getCardNumber());
        assertEquals("1", contractHolder1.getContractHolderId());
        assertEquals("type1", contractHolder1.getContractHolderType());
        assertEquals("2", contractHolder2.getContractHolderId());
        assertEquals("type2", contractHolder2.getContractHolderType());
    }

    /**
     * Encode exception.
     */
    @Test
    public void encodeException() {
        IReflectionEncoder encoder = new ReflectionEncoder(new IStringEncoder() {

            @Override
            public String encode(String value) {
                return "#$#" + value;
            }
        }, Arrays.asList((IObjectEncoder) new AbstractObjectEncoder < CardInfo >() {

            @Override
            protected CardInfo internalEncodeObject(CardInfo obj, IStringEncoder stringEncoder) throws EncoderException {
                throw new EncoderException("boum");
            }
        }, (IObjectEncoder) new ContractHolderEncrypter()));

        Card card = new Card();
        CardInfo cardInfo = new CardInfo();
        cardInfo.setCardNumber("1111222233334444");
        cardInfo.setBrandDesc("test");
        card.setCardInfo(cardInfo);

        ContractHolder contractHolder1 = new ContractHolder();
        contractHolder1.setContractHolderId("1");
        contractHolder1.setContractHolderType("type1");

        ContractHolder contractHolder2 = new ContractHolder();
        contractHolder2.setContractHolderId("2");
        contractHolder2.setContractHolderType("type2");
        card.setContractHolder((ContractHolder[]) Arrays.asList(contractHolder1, contractHolder2, contractHolder1).toArray());

        encoder.encodeByReflection(card);

        assertNotNull(card);
        assertEquals("test", cardInfo.getBrandDesc());
        assertEquals("1111222233334444", cardInfo.getCardNumber());
        assertEquals("#$#1", contractHolder1.getContractHolderId());
        assertEquals("type1", contractHolder1.getContractHolderType());
        assertEquals("#$#2", contractHolder2.getContractHolderId());
        assertEquals("type2", contractHolder2.getContractHolderType());
    }

    /**
     * Interface black list.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void interfaceBlackList() {
        IReflectionEncoder encoder = new ReflectionEncoder(new AbstractStringEncoder() {
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
        IReflectionEncoder encoder = new ReflectionEncoder(new AbstractStringEncoder() {

            @Override
            public String encode(String value) {
                return "#$#" + value;
            }
        });
        encoder.setMaxDeep(6);
        assertEquals(6, ((ReflectionEncoder) encoder).getMaxDeep());
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
        IReflectionEncoder encoder = new ReflectionEncoder(new IStringEncoder() {

            @Override
            public String encode(String value) {
                return "#$#" + value;
            }
        }, Arrays.asList((IObjectEncoder) new SpecificObjectToEncodeEncoder()));

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
