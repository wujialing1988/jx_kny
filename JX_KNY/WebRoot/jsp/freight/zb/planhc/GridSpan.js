/*
**合并单元格的函数，合并表格内所有连续的具有相同值的单元格。调用方法示例：
**store.on("load",function(){gridSpan(grid,"row","[FbillNumber],[FbillDate],[FAudit],[FAuditDate],[FSure],[FSureDate]","FbillNumber");});
**参数：grid-需要合并的表格,rowOrCol-合并行还是列,cols-需要合并的列（行合并的时候有效）,sepCols以哪个列为分割(即此字段不合并的2行，其他字段也不许合并)，默认为空
*/
function gridSpan(grid, rowOrCol, cols, sepCol){
    var array1 = new Array();
    var arraySep = new Array();
    var count1 = 0;
    var count2 = 0;
    var index1 = 0;
    var index2 = 0;
    var aRow = undefined;
    var preValue = undefined;
    var firstSameCell = 0;
    var allRecs = grid.getStore().getRange();
    if(rowOrCol == "row"){
        count1 = grid.getColumnModel().getColumnCount();
        count2 = grid.getStore().getCount();
    } else {
        count1 = grid.getStore().getCount();
        count2 = grid.getColumnModel().getColumnCount();
    }
    for(i = 0; i < count1; i++){
        if(rowOrCol == "row"){
            var curColName = grid.getColumnModel().getDataIndex(i);
            var curCol = "[" + curColName + "]";
            if(cols.indexOf(curCol) < 0)
            continue;
        }
        preValue = undefined;
        firstSameCell = 0;
        array1[i] = new Array();
        for(j = 0; j < count2; j++){
            if(rowOrCol == "row"){
                index1 = j;
                index2 = i;
            } else {
                index1 = i;
                index2 = j;
            }
            var colName = grid.getColumnModel().getDataIndex(index2);
            if(sepCol && colName == sepCol)
            arraySep[index1] = allRecs[index1].get(sepCol);
            var seqOldValue = seqCurValue = "1";
            if(sepCol && index1 > 0){
                seqOldValue = arraySep[index1 - 1];
                seqCurValue = arraySep[index1];
            }
             
            if(allRecs[index1].get(colName) == preValue && (colName == sepCol || seqOldValue == seqCurValue)){
//                 alert(colName + "======" + seqOldValue + "======" + seqCurValue);
                 allRecs[index1].set(colName, "");
                 array1[i].push(j);
                 if(j == count2 - 1){
                     var index = firstSameCell + Math.round((j + 1 - firstSameCell) / 2 - 1);
                     if(rowOrCol == "row"){
                         allRecs[index].set(colName, preValue);
                       } else {
                           allRecs[index1].set(grid.getColumnModel().getColumnId(index), preValue);
                       }
                   }
               } else {
                   if(j != 0){
                       var index = firstSameCell + Math.round((j + 1 - firstSameCell) / 2 - 1);
                       if(rowOrCol == "row"){
                           allRecs[index].set(colName, preValue);
                       } else {
                           allRecs[index1].set(grid.getColumnModel().getColumnId(index), preValue);
                    }
                   }
               firstSameCell = j;
               preValue = allRecs[index1].get(colName);
               allRecs[index1].set(colName, "");
               if(j == count2 - 1){
                   allRecs[index1].set(colName, preValue);
               }
           }
        }
    }
    grid.getStore().commitChanges();
    //添加所有分隔线
    var rCount = grid.getStore().getCount();
    for(i = 0; i < rCount; i ++){
        for(j = 0; j < grid.getColumnModel().getColumnCount(); j ++){
               aRow = grid.getView().getCell(i,j);
             if(i == 0){
                 aRow.style.borderTop = "none";
                 aRow.style.borderLeft = "1px solid #ccc";
             }else if(i == rCount - 1){
                 aRow.style.borderTop = "1px solid #ccc";
                 aRow.style.borderLeft = "1px solid #ccc";
                aRow.style.borderBottom = "1px solid #ccc";
             }else{
                 aRow.style.borderTop = "1px solid #ccc";
                 aRow.style.borderLeft = "1px solid #ccc";
             }
             if(j == grid.getColumnModel().getColumnCount()-1)
                 aRow.style.borderRight = "1px solid #ccc";
            if(i == rCount-1)     
             aRow.style.borderBottom = "1px solid #ccc";
           }
       }
       //去除合并的单元格的分隔线
       for(i = 0; i < array1.length; i++){
           if(!Ext.isEmpty(array1[i])){
               for(j = 0; j < array1[i].length; j++){
                   if(rowOrCol == "row"){
                       aRow = grid.getView().getCell(array1[i][j],i);
                       aRow.style.borderTop = "none";
                   } else {
                       aRow = grid.getView().getCell(i, array1[i][j]);
                       aRow.style.borderLeft = "none";
                   }
               }
           }
       }
}