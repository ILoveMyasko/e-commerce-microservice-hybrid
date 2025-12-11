import http from 'k6/http';
import { check, sleep } from 'k6';

// Настройки нагрузки
export const options = {
    stages: [
        { duration: '5s', target: 10 },  // Разгон: за 10с поднимаем до 10 пользователей
        { duration: '20s', target: 20 },  // Нагрузка: держим 50 пользователей 30с
        { duration: '5s', target: 0 },   // Остывание
    ],
};

export default function () {
    // GraphQL запрос
    const query = `
    query {
      products {
        id
        name,
        description
        price
        priceHistory
      }
    }
  `;

    const headers = {
        'Content-Type': 'application/json',
    };

    // Отправляем запрос на Gateway
    const res = http.post('http://localhost:8080/graphql', JSON.stringify({ query: query }), { headers: headers });

    // Проверки (Assertions)
    check(res, {
        'is status 200': (r) => r.status === 200,
        'no errors': (r) => r.body.indexOf('errors') === -1,
        'timing < 500ms': (r) => r.timings.duration < 500, // Хотим, чтобы ответ был быстрее 500мс
    });

    sleep(1); // Имитация реального пользователя (пауза 1 сек между запросами)
}