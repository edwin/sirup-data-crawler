package com.edw.mapper;

import com.edw.model.TblKlpd;
import com.edw.model.TblSatker;

import java.util.List;

/**
 * <pre>
 *  com.edw.mapper.TblSatkerMapper
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com >
 * 27 Apr 2026 15:47
 */

public interface TblSatkerMapper {

    TblSatker getSatkerById(String idSatker);

    int insertSatker(TblSatker tblSatker);

    int updateSatker(TblSatker tblSatker);

    List<String> getAllSatkerId();

    void insertSatkerList(List<TblSatker> list);
}
