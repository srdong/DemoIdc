INSERT INTO `dic` VALUES (1, NULL, 'IDC_EC', '总耗电', NULL, NULL, NULL, 1);
INSERT INTO `dic` VALUES (2, 1, 'IDC_EC_Supply', '市电', 'A', '市电', 'kWh', 1);
INSERT INTO `dic` VALUES (3, 1, 'IDC_EC_DieselGenerator', '柴油发电', 'A1', '柴油发电机', 'kWh', 1);
INSERT INTO `dic` VALUES (4, 1, 'IDC_EC_Renewable', '可再生能源发电', 'A2', '太阳能发电', 'kWh', 1);
INSERT INTO `dic` VALUES (5, 1, 'IDC_EC_Gas', '燃气发电', 'A3', '燃气发电机', 'kWh', 1);
INSERT INTO `dic` VALUES (6, 1, 'IDC_EC_Storage', '蓄电站输出电', 'X', '蓄电站', 'kWh', 1);
INSERT INTO `dic` VALUES (7, NULL, 'IDC_IT_EC', 'IT设备耗电', NULL, NULL, NULL, 1);
INSERT INTO `dic` VALUES (8, 7, 'IDC_ITEC_Ups_In', 'UPS进线侧电耗', 'E', 'UPS进', 'kWh', 1);
INSERT INTO `dic` VALUES (9, 7, 'IDC_ITEC_Ups_Out', 'UPS出线侧电耗', 'B', 'UPS出', 'kWh', 1);
INSERT INTO `dic` VALUES (10, 7, 'IDC_ITEC_Cabinet', '列头柜配电', 'C', '列头柜配电', 'kWh', 1);
INSERT INTO `dic` VALUES (11, 7, 'IDC_ITEC_ITEquipment', 'IT设备实际耗电', 'D', 'IT设备负载', 'kWh', 1);
INSERT INTO `dic` VALUES (12, NULL, 'IDC_Cooling_EC', '制冷设备耗电', NULL, NULL, NULL, 1);
INSERT INTO `dic` VALUES (13, 12, 'IDC_Cooling_System', '冷源系统耗电', 'E1', '制冷系统', 'kWh', 1);
INSERT INTO `dic` VALUES (14, 12, 'IDC_Ups_Cooling', 'UPS制冷耗电', 'B1', '制冷系统', 'kWh', 1);
INSERT INTO `dic` VALUES (15, 12, 'IDC_Cabinet_Cooling', '机柜制冷耗电', 'B2', '列间、机柜制冷', 'kWh', 1);
INSERT INTO `dic` VALUES (16, NULL, 'IDC_Other_EC', '数据中心其他耗电', NULL, NULL, NULL, 1);
INSERT INTO `dic` VALUES (17, 16, 'IDC_SubMetering_Office', '数据中心办公用电', 'E2', '其他配套系统用电', 'kWh', 1);
INSERT INTO `dic` VALUES (18, NULL, 'Non_IDC_EC', '非数据中心用电', 'E3', '非数据中心用电', 'kWh', 1);
INSERT INTO `dic` VALUES (19, NULL, 'IDC_Consumption_Water', '数据中心水资源消耗', NULL, NULL, NULL, 1);
INSERT INTO `dic` VALUES (20, 19, 'Refrigeration_Station', '冷冻站用水', 'W1', '制冷站用水', '立方米', 1);
INSERT INTO `dic` VALUES (21, 19, 'Cooling_Tower', '冷却塔用水', 'W2', '冷却塔用水', '立方米', 1);
INSERT INTO `dic` VALUES (22, NULL, 'IDC_MP', '温湿度测量', NULL, NULL, NULL, 2);
INSERT INTO `dic` VALUES (23, 22, 'IDC_MP_Temperature_Indoor', '室内温度', NULL, NULL, '摄氏度', 2);
INSERT INTO `dic` VALUES (24, 22, 'IDC_MP_Temperature_Outdoor', '室外温度', NULL, NULL, '摄氏度', 2);
INSERT INTO `dic` VALUES (25, 22, 'IDC_MP_Humidity_Indoor', '室内湿度', NULL, NULL, '%', 2);
INSERT INTO `dic` VALUES (26, 22, 'IDC_MP_Humidity_Outdoor', '室外湿度', NULL, NULL, '%', 2);

INSERT INTO `dic_pue` VALUES (1, 'D标识计量修正', 'A|A1|A2|A3|E3', 'A+A1+A2+A3-E3', 'D', 'D');
INSERT INTO `dic_pue` VALUES (2, 'C标识计量修正', 'A|A1|A2|A3|E3', 'A+A1+A2+A3-E3', 'C|a1', 'C*a1');
INSERT INTO `dic_pue` VALUES (3, 'B标识计量修正_B1标识无法计量', 'A|A1|A2|A3|E3', 'A+A1+A2+A3-E3', 'B|a2', 'B*a2');
INSERT INTO `dic_pue` VALUES (4, 'B标识计量修正_B1标识可计量', 'A|A1|A2|A3|E3', 'A+A1+A2+A3-E3', 'B|B1|a1', '(B-B1)*a1');
INSERT INTO `dic_pue` VALUES (5, 'E标识计量修正_B1标识无法计量', 'A|A1|A2|A3|E3', 'A+A1+A2+A3-E3', 'E|a2|a3', 'E*a2*a3');
INSERT INTO `dic_pue` VALUES (6, 'E标识计量修正_B1标识可计量', 'A|A1|A2|A3|E3', 'A+A1+A2+A3-E3', 'E|B1|a1|a3', '(E*a3-B1)*a1');

INSERT INTO `idc_user`(`id`, `username`, `password`) VALUES (1, 'admin', '21232f297a57a5a743894a0e4a801fc3');

INSERT INTO `limit_ranges`(`id`, `lower_limit`, `upper_limit`, `val1`, `val2`, `val3`) VALUES (1, '', '0.25', '0.93', '0.61', '0.6');
INSERT INTO `limit_ranges`(`id`, `lower_limit`, `upper_limit`, `val1`, `val2`, `val3`) VALUES (2, '0.25', '0.5', '0.95', '0.7', '0.8');
INSERT INTO `limit_ranges`(`id`, `lower_limit`, `upper_limit`, `val1`, `val2`, `val3`) VALUES (3, '0.5', '0.75', '0.96', '0.8', '0.9');
INSERT INTO `limit_ranges`(`id`, `lower_limit`, `upper_limit`, `val1`, `val2`, `val3`) VALUES (4, '0.75', '', '0.97', '0.87', '0.9');

INSERT INTO `low_frequency`(`id`, `value`, `unit`, `start_time`, `end_time`, `type`, `type2`) VALUES (1, 0.00, '', NULL, NULL, 'IDC_ITEquipment_Load', 'IDC_RF');
INSERT INTO `low_frequency`(`id`, `value`, `unit`, `start_time`, `end_time`, `type`, `type2`) VALUES (2, 0.00, 'kW', NULL, NULL, 'IDC_RF_Power_LiquidCooling', 'IDC_RF');
INSERT INTO `low_frequency`(`id`, `value`, `unit`, `start_time`, `end_time`, `type`, `type2`) VALUES (3, 0.00, 'kW', NULL, NULL, 'IDC_RF_Power_TotalOperating', 'IDC_RF');
INSERT INTO `low_frequency`(`id`, `value`, `unit`, `start_time`, `end_time`, `type`, `type2`) VALUES (4, 0.00, 'kWh', NULL, NULL, 'IDC_RF_Renewable', 'IDC_RF');
INSERT INTO `low_frequency`(`id`, `value`, `unit`, `start_time`, `end_time`, `type`, `type2`) VALUES (5, 0.00, 'kWh', NULL, NULL, 'IDC_RF_EC', 'IDC_RF');
INSERT INTO `low_frequency`(`id`, `value`, `unit`, `start_time`, `end_time`, `type`, `type2`) VALUES (6, 0.00, 'kWh', NULL, NULL, 'IDC_RF_StorageStation_DisChargeCapacity', 'IDC_RF');
INSERT INTO `low_frequency`(`id`, `value`, `unit`, `start_time`, `end_time`, `type`, `type2`) VALUES (7, 0.00, 'kWh', NULL, NULL, 'IDC_RF_OffPeakStorage_CoolingCapacity', 'IDC_RF');
INSERT INTO `low_frequency`(`id`, `value`, `unit`, `start_time`, `end_time`, `type`, `type2`) VALUES (8, 0.00, 'kWh', NULL, NULL, 'IDC_RF_Total_CoolingCapacity', 'IDC_RF');
INSERT INTO `low_frequency`(`id`, `value`, `unit`, `start_time`, `end_time`, `type`, `type2`) VALUES (9, 0.00, 'm³', NULL, NULL, 'IDC_Consumption_Water', 'IDC_Consumption');
INSERT INTO `low_frequency`(`id`, `value`, `unit`, `start_time`, `end_time`, `type`, `type2`) VALUES (10, 0.00, 'm³', NULL, NULL, 'IDC_Consumption_Gas', 'IDC_Consumption');
INSERT INTO `low_frequency`(`id`, `value`, `unit`, `start_time`, `end_time`, `type`, `type2`) VALUES (11, 0.00, 't', NULL, NULL, 'IDC_Consumption_DieselOil', 'IDC_Consumption');

INSERT INTO `scheduler_cron`(`id`, `name`, `cron`, `state`) VALUES (1, 'HighFrequencyTask', '0 0,15,30,45 * * * ?', 1);
INSERT INTO `scheduler_cron`(`id`, `name`, `cron`, `state`) VALUES (2, 'BreakPointTask', '0 5,20,35,50 * * * ?', 1);
INSERT INTO `scheduler_cron`(`id`, `name`, `cron`, `state`) VALUES (3, 'UnsuccessfulDataTask', '0 3,18,33,48 * * * ?', 1);
INSERT INTO `scheduler_cron`(`id`, `name`, `cron`, `state`) VALUES (4, 'PullTimeDelTask', '0 5 8 1 * ?', 1);
