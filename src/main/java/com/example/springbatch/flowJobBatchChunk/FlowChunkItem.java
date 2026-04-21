package com.example.springbatch.flowJobBatchChunk;

public class FlowChunkItem {

    private Long id;
    private String sourceValue;
    private String processedValue;

    public FlowChunkItem(Long id, String sourceValue) {
        this.id = id;
        this.sourceValue = sourceValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceValue() {
        return sourceValue;
    }

    public void setSourceValue(String sourceValue) {
        this.sourceValue = sourceValue;
    }

    public String getProcessedValue() {
        return processedValue;
    }

    public void setProcessedValue(String processedValue) {
        this.processedValue = processedValue;
    }

    @Override
    public String toString() {
        return "FlowChunkItem{" +
                "id=" + id +
                ", sourceValue='" + sourceValue + '\'' +
                ", processedValue='" + processedValue + '\'' +
                '}';
    }
}
