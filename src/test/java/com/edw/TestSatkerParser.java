package com.edw;

import com.edw.model.TblSatker;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple test to verify TblSatker JSON parsing
 */
public class TestSatkerParser {

    public static void main(String[] args) {
        TestSatkerParser test = new TestSatkerParser();
        test.testParseJsonToTblSatker();
    }

    public void testParseJsonToTblSatker() {
        // Sample JSON response from the issue description
        String jsonResponse = "{\"aaData\":[[\"65390\",\"Badan Kesatuan Bangsa dan Politik\",\"10\",\"237\",\"86\",\"3132\",\"113\",\"2265\",\"209\",\"5635\",\"false\",\"4\"],[\"65402\",\"Badan Penanggulangan Bencana Daerah\",\"54\",\"8422\",\"6\",\"3624\",\"19\",\"641\",\"79\",\"12687\",\"false\",\"4\"]],\"iTotalDisplayRecords\":56,\"sEcho\":2}";
        String idKlpd = "TEST_KLPD_123";

        List<TblSatker> satkerList = parseJsonToTblSatker(jsonResponse, idKlpd);

        System.out.println("Parsed " + satkerList.size() + " Satker records:");
        for (TblSatker satker : satkerList) {
            System.out.println("ID: " + satker.getIdSatker() + 
                             ", Name: " + satker.getNamaSatker() + 
                             ", KLPD: " + satker.getIdKlpd());
        }

        // Verify expected results
        if (satkerList.size() == 2) {
            TblSatker first = satkerList.get(0);
            TblSatker second = satkerList.get(1);

            if ("65390".equals(first.getIdSatker()) && 
                "Badan Kesatuan Bangsa dan Politik".equals(first.getNamaSatker()) &&
                idKlpd.equals(first.getIdKlpd()) &&
                "65402".equals(second.getIdSatker()) &&
                "Badan Penanggulangan Bencana Daerah".equals(second.getNamaSatker()) &&
                idKlpd.equals(second.getIdKlpd())) {
                
                System.out.println("✓ TEST PASSED: JSON parsing works correctly!");
            } else {
                System.out.println("✗ TEST FAILED: Parsed data doesn't match expected values");
            }
        } else {
            System.out.println("✗ TEST FAILED: Expected 2 records, got " + satkerList.size());
        }
    }

    private List<TblSatker> parseJsonToTblSatker(String jsonResponse, String idKlpd) {
        List<TblSatker> satkerList = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            // Get the aaData array
            JsonNode aaDataNode = rootNode.get("aaData");

            if (aaDataNode != null && aaDataNode.isArray()) {
                for (JsonNode arrayNode : aaDataNode) {
                    if (arrayNode.isArray() && arrayNode.size() >= 2) {
                        // Map first element to idSatker and second element to namaSatker
                        String idSatker = arrayNode.get(0).asText();
                        String namaSatker = arrayNode.get(1).asText();

                        TblSatker satker = new TblSatker(idSatker, namaSatker, idKlpd);
                        satkerList.add(satker);

                        System.out.println("Parsed Satker: " + idSatker + " - " + namaSatker + " - KLPD: " + idKlpd);
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error parsing JSON response for KLPD: " + idKlpd + " - " + e.getMessage());
        }

        return satkerList;
    }
}