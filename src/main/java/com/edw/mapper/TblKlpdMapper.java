package com.edw.mapper;

import com.edw.model.TblKlpd;
import java.util.List;

/**
 * <pre>
 *  com.edw.service.TblKlpdMapper
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com >
 * 27 Apr 2026 12:21
 */

public interface TblKlpdMapper {

    TblKlpd getKlpdById(String idKlpd);

    int insertKlpd(TblKlpd tblKlpd);

    int updateKlpd(TblKlpd tblKlpd);

    List<String> getAllKlpdIds();

}
