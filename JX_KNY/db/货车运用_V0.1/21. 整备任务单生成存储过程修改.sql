CREATE OR REPLACE PROCEDURE sp_update_jt6 (i_rdp_idx VARCHAR2,                        --整备单IDX
                                               i_siteID VARCHAR2                --站场id
                                               )
   AS
      v_jt6_status   zb_zbgl_jt6.fault_notice_status%TYPE;
   BEGIN
      v_jt6_status := 'TODO';

      UPDATE zb_zbgl_jt6
         SET rdp_idx = i_rdp_idx,
             fault_notice_status = v_jt6_status,
             REPAIR_CLASS = '10',
             SITEID = i_siteID,
             update_time = sysdate
       WHERE record_status = 0 AND fault_notice_status = 'INITIALIZE'
             AND (train_type_idx, train_no) =
                    (SELECT train_type_idx, train_no
                       FROM zb_zbgl_rdp
                      WHERE idx = i_rdp_idx);
   END sp_update_jt6;
