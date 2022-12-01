package com.huawei.excelhandle;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.Getter;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;

@Getter
public class StringExcelListener extends AnalysisEventListener<T> {

    private List<T> datas = new ArrayList<>();

    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        datas.add(t);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


}
