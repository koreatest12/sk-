// 장비 정의 (실제 운영에서는 인벤토리 시스템에서 가져오세요)
export const DEVICES = [
  { name: "CORE-SW-01", type: "Switch", ifs: ["Gi0/1", "Gi0/2", "Te1/1"] },
  { name: "EDGE-RTR-02", type: "Router", ifs: ["Gi0/0", "Gi0/1"] },
  { name: "DB-SRV-03", type: "Server", ifs: ["eth0", "eth1"] },
  { name: "WEB-SRV-04", type: "Server", ifs: ["eth0"] },
  { name: "FW-05", type: "Firewall", ifs: ["port1", "port2", "port3"] },
];
