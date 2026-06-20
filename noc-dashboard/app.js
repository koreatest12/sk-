// NOC 대시보드 — 데모 데이터 기반 실시간 표시
// 실제 운영에서는 fetchDevices()를 본인 모니터링 API 호출로 교체하세요.

const DEVICES = [
  { name: "CORE-SW-01", type: "Switch", ifs: ["Gi0/1", "Gi0/2", "Te1/1"] },
  { name: "EDGE-RTR-02", type: "Router", ifs: ["Gi0/0", "Gi0/1"] },
  { name: "DB-SRV-03", type: "Server", ifs: ["eth0", "eth1"] },
  { name: "WEB-SRV-04", type: "Server", ifs: ["eth0"] },
  { name: "FW-05", type: "Firewall", ifs: ["port1", "port2", "port3"] },
];

function rnd(min, max) { return Math.floor(Math.random() * (max - min + 1)) + min; }
function statusOf(load) { return load < 70 ? "ok" : load < 90 ? "warn" : "crit"; }
function badge(s) {
  const map = { ok: ["b-ok", "UP"], warn: ["b-warn", "WARN"], crit: ["b-crit", "DOWN"] };
  const [cls, label] = map[s];
  return `<span class="badge ${cls}">${label}</span>`;
}

// 실제 데이터 소스로 교체할 지점 (현재는 데모 난수)
function fetchDevices() {
  return DEVICES.map(d => ({
    ...d,
    ifs: d.ifs.map(i => { const load = rnd(10, 99); return { name: i, load, status: statusOf(load) }; })
  }));
}

function render() {
  const data = fetchDevices();
  let up = 0, warn = 0, crit = 0, total = 0;
  data.forEach(d => d.ifs.forEach(i => {
    total++; if (i.status === "ok") up++; else if (i.status === "warn") warn++; else crit++;
  }));

  document.getElementById("summary").innerHTML = `
    <div class="kpi"><div class="label">총 인터페이스</div><div class="value">${total}</div></div>
    <div class="kpi"><div class="label">정상</div><div class="value" style="color:var(--ok)">${up}</div></div>
    <div class="kpi"><div class="label">경고</div><div class="value" style="color:var(--warn)">${warn}</div></div>
    <div class="kpi"><div class="label">위험</div><div class="value" style="color:var(--crit)">${crit}</div></div>`;

  document.getElementById("deviceGrid").innerHTML = data.map(d => `
    <div class="card">
      <h3>${d.name} <small style="color:#8b949e">(${d.type})</small></h3>
      ${d.ifs.map(i => `
        <div class="row"><span>${i.name} · ${i.load}%</span>${badge(i.status)}</div>`).join("")}
    </div>`).join("");
}

function tickClock() {
  document.getElementById("clock").textContent = new Date().toLocaleTimeString("ko-KR");
}

setInterval(tickClock, 1000); tickClock();
setInterval(render, 5000); render();
