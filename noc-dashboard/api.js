// 모니터링 데이터 소스. 지금은 데모(난수). 실제 API로 교체하세요.
import { DEVICES } from "./data.js";

const rnd = (a, b) => Math.floor(Math.random() * (b - a + 1)) + a;
export const statusOf = (load) => (load < 70 ? "ok" : load < 90 ? "warn" : "crit");

export function fetchDevices() {
  return DEVICES.map((d) => ({
    ...d,
    ifs: d.ifs.map((i) => { const load = rnd(10, 99); return { name: i, load, status: statusOf(load) }; }),
  }));
}
