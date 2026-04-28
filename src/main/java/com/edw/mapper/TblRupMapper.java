package com.edw.mapper;

import com.edw.model.TblRup;

import java.util.List;

/**
 * <pre>
 *  com.edw.mapper.TblRupMapper
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com >
 * 27 Apr 2026 19:31
 */
public interface TblRupMapper {

    TblRup getRupById(String idRup);

    int insertRup(TblRup tblRup);

    int updateRup(TblRup tblRup);

    void insertRupList(List<TblRup> list);

}
