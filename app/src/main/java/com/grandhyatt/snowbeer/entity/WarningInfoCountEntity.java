package com.grandhyatt.snowbeer.entity;

/**
 * 预警信息消息条数
 * Created by ycm on 2018/9/28.
 */

public class WarningInfoCountEntity {

    /// <summary>
    /// 设备维修提醒
    /// </summary>
    private int EquipRepairPlanCount;
    /// <summary>
    /// 设备保养提醒
    /// </summary>
    private int EquipMaintenPlanCount;
    /// <summary>
    /// 设备检验提醒
    /// </summary>
    private int EquipInspectPlanCount;
    /// <summary>
    /// 备件更换提醒
    /// </summary>
    private int EquipSpareReplacePlanCount;
    /// <summary>
    /// 设备外委维修提醒
    /// </summary>
    private int EquipRepairExPlanCount;
    /// <summary>
    /// 建筑物维修提醒
    /// </summary>
    private int BuildRepairPlanCount;
    /// <summary>
    /// 化学仪器维修按次数提醒
    /// </summary>
    private int AssaySpareReplaceByTimeCount;
    /// <summary>
    /// 化学仪器维修按周期提醒
    /// </summary>
    private int AssaySpareReplaceByCycleCount;
    /// <summary>
    /// 化学仪器维修计划提醒
    /// </summary>
    private int AssayRepairPlanCount;

    public WarningInfoCountEntity() {
    }

    public int getEquipRepairPlanCount() {
        return EquipRepairPlanCount;
    }

    public void setEquipRepairPlanCount(int equipRepairPlanCount) {
        EquipRepairPlanCount = equipRepairPlanCount;
    }

    public int getEquipMaintenPlanCount() {
        return EquipMaintenPlanCount;
    }

    public void setEquipMaintenPlanCount(int equipMaintenPlanCount) {
        EquipMaintenPlanCount = equipMaintenPlanCount;
    }

    public int getEquipInspectPlanCount() {
        return EquipInspectPlanCount;
    }

    public void setEquipInspectPlanCount(int equipInspectPlanCount) {
        EquipInspectPlanCount = equipInspectPlanCount;
    }

    public int getEquipSpareReplacePlanCount() {
        return EquipSpareReplacePlanCount;
    }

    public void setEquipSpareReplacePlanCount(int equipSpareReplacePlanCount) {
        EquipSpareReplacePlanCount = equipSpareReplacePlanCount;
    }

    public int getEquipRepairExPlanCount() {
        return EquipRepairExPlanCount;
    }

    public void setEquipRepairExPlanCount(int equipRepairExPlanCount) {
        EquipRepairExPlanCount = equipRepairExPlanCount;
    }

    public int getBuildRepairPlanCount() {
        return BuildRepairPlanCount;
    }

    public void setBuildRepairPlanCount(int buildRepairPlanCount) {
        BuildRepairPlanCount = buildRepairPlanCount;
    }

    public int getAssaySpareReplaceByTimeCount() {
        return AssaySpareReplaceByTimeCount;
    }

    public void setAssaySpareReplaceByTimeCount(int assaySpareReplaceByTimeCount) {
        AssaySpareReplaceByTimeCount = assaySpareReplaceByTimeCount;
    }

    public int getAssaySpareReplaceByCycleCount() {
        return AssaySpareReplaceByCycleCount;
    }

    public void setAssaySpareReplaceByCycleCount(int assaySpareReplaceByCycleCount) {
        AssaySpareReplaceByCycleCount = assaySpareReplaceByCycleCount;
    }

    public int getAssayRepairPlanCount() {
        return AssayRepairPlanCount;
    }

    public void setAssayRepairPlanCount(int assayRepairPlanCount) {
        AssayRepairPlanCount = assayRepairPlanCount;
    }
}
