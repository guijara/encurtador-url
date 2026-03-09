// src/lib/api.ts
import axios from 'axios';
import toast from 'react-hot-toast';

// Instância base do Axios
export const api = axios.create({
    baseURL: 'http://localhost:8080',
    headers: {
        'Content-Type': 'application/json',
    },
});

// Interceptor de Requisição: Adiciona o Token
api.interceptors.request.use((config) => {
    // Garantir que estamos no cliente antes de acessar localStorage
    if (typeof window !== 'undefined') {
        const token = localStorage.getItem('token');
        // Adiciona o token apenas nas rotas que começam com /api/
        if (token && config.url?.startsWith('/api/')) {
            config.headers.Authorization = `Bearer ${token}`;
        }
    }
    return config;
});

// Interceptor de Resposta: Tratamento global de erros
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response) {
            const { status, data } = error.response;

            switch (status) {
                case 401:
                    toast.error('Sessão expirada. Por favor, faça login novamente.');
                    if (typeof window !== 'undefined') {
                        localStorage.removeItem('token');
                        window.location.href = '/login';
                    }
                    break;
                case 410:
                    toast.error('Este link já expirou.');
                    break;
                case 429:
                    toast.error('Limite de requisições excedido. Tente novamente mais tarde.');
                    break;
                default:
                    toast.error(data?.message || 'Ocorreu um erro inesperado.');
            }
        } else {
            toast.error('Erro de conexão com o servidor.');
        }
        return Promise.reject(error);
    }
);