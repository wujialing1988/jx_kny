CREATE OR REPLACE VIEW V_K_MARSHALLING_TRAIN_COUNT AS
     
SELECT rownum AS IDX, B.* from (SELECT  T.MARSHALLING_CODE,
          T.T_VEHICLE_KIND_NAME || '('||COUNT(T.T_VEHICLE_KIND_CODE) || ')' AS   T_VEHICLE_KIND_NAME_COUNT ,T.T_VEHICLE_KIND_CODE
  
  FROM K_MARSHALLING_TRAIN T    WHERE T.RECORD_STATUS = 0
 GROUP BY T.MARSHALLING_CODE, T.T_VEHICLE_KIND_CODE, T.T_VEHICLE_KIND_NAME )  B
 
                     