/**
 * Мокований REST API для Практичної роботи №3 (варіант 6 — біогазова установка).
 *
 * Запуск:
 *   cd mock-server
 *   npm install
 *   npm start
 *
 * Сервер слухає порт 5055. З Android-емулятора він доступний за адресою
 * http://10.0.2.2:5055/ (10.0.2.2 — спеціальна адреса, якою емулятор
 * звертається до localhost хост-машини).
 */
const express = require("express");
const cors = require("cors");

const app = express();
app.use(cors());
app.use(express.json());

const PORT = 5055;

// ---------- "База даних" показників реактора (в пам'яті) ----------
let readings = [
  { id: 1, fillLevel: 62, methaneConcentration: 58, temperature: 36, pressure: 118, timestamp: Date.now() - 3 * 3600_000 },
  { id: 2, fillLevel: 71, methaneConcentration: 64, temperature: 37, pressure: 122, timestamp: Date.now() - 2 * 3600_000 },
  { id: 3, fillLevel: 80, methaneConcentration: 45, temperature: 39, pressure: 130, timestamp: Date.now() - 1 * 3600_000 },
  { id: 4, fillLevel: 93, methaneConcentration: 28, temperature: 41, pressure: 140, timestamp: Date.now() },
];
let nextId = 5;

// ---------- Користувачі для мокованої автентифікації ----------
const USERS = [
  { username: "admin", password: "admin123", role: "ADMIN" },
  { username: "operator", password: "operator123", role: "OPERATOR" },
];

// Імітація підписаного JWT: НЕ для реального використання, лише для навчання.
function makeMockedToken(user) {
  const header = Buffer.from(JSON.stringify({ alg: "mock", typ: "JWT" })).toString("base64url");
  const payload = Buffer.from(JSON.stringify({
    sub: user.username,
    role: user.role,
    iat: Date.now(),
    exp: Date.now() + 15 * 60 * 1000, // токен дійсний 15 хвилин
  })).toString("base64url");
  const signature = Buffer.from(`mocked-signature-${user.username}`).toString("base64url");
  return `${header}.${payload}.${signature}`;
}

function decodeMockedToken(token) {
  try {
    const parts = token.replace("Bearer ", "").split(".");
    if (parts.length !== 3) return null;
    const payload = JSON.parse(Buffer.from(parts[1], "base64url").toString("utf-8"));
    if (payload.exp < Date.now()) return null; // протермінований токен
    return payload;
  } catch {
    return null;
  }
}

// ---------- Заняття №9-10: дані показників реактора ----------
app.get("/readings", (req, res) => {
  res.json(readings);
});

app.get("/readings/:id", (req, res) => {
  const reading = readings.find((r) => r.id === Number(req.params.id));
  if (!reading) return res.status(404).json({ message: "Показник не знайдено" });
  res.json(reading);
});

app.post("/readings", (req, res) => {
  const { fillLevel, methaneConcentration, temperature, pressure } = req.body;
  if (
    [fillLevel, methaneConcentration, temperature, pressure].some(
      (v) => typeof v !== "number"
    )
  ) {
    return res.status(400).json({ message: "Некоректні дані показника" });
  }
  const reading = { id: nextId++, fillLevel, methaneConcentration, temperature, pressure, timestamp: Date.now() };
  readings.push(reading);
  res.status(201).json(reading);
});

// ---------- Заняття №11: автентифікація через мокований токен ----------
app.post("/auth/login", (req, res) => {
  const { username, password } = req.body;
  const user = USERS.find((u) => u.username === username && u.password === password);
  if (!user) {
    return res.status(401).json({ message: "Неправильний логін або пароль" });
  }
  res.json({ token: makeMockedToken(user), role: user.role });
});

app.get("/secure/status", (req, res) => {
  const authHeader = req.headers["authorization"];
  if (!authHeader) {
    return res.status(401).json({ message: "Відсутній токен авторизації" });
  }
  const payload = decodeMockedToken(authHeader);
  if (!payload) {
    return res.status(401).json({ message: "Недійсний або протермінований токен" });
  }
  res.json({
    user: payload.sub,
    role: payload.role,
    reactorsOnline: 3,
    averageMethane:
      readings.reduce((sum, r) => sum + r.methaneConcentration, 0) / readings.length,
    adminNote:
      payload.role === "ADMIN"
        ? "Доступно адміністративне керування реактором"
        : null,
  });
});

// ---------- Заняття №12: нестабільний ендпоінт для демонстрації помилок ----------
// Випадково повертає успіх, 500, 503 або "зависає" на 12с (щоб спрацював таймаут клієнта).
app.get("/unstable/forecast", (req, res) => {
  const roll = Math.random();
  if (roll < 0.34) {
    return res.status(500).json({ message: "Внутрішня помилка сервера" });
  }
  if (roll < 0.6) {
    return res.status(503).json({ message: "Сервіс тимчасово недоступний" });
  }
  if (roll < 0.75) {
    setTimeout(() => res.json({ forecastMethane: 55, note: "Запит виконано з затримкою" }), 12000);
    return;
  }
  res.json({ forecastMethane: 55 + Math.round(Math.random() * 10), note: "ОК" });
});

app.listen(PORT, () => {
  console.log(`Biogas mock API running at http://localhost:${PORT}`);
  console.log(`From the Android emulator use http://10.0.2.2:${PORT}`);
});
