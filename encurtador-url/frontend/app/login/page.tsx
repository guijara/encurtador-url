// src/app/login/page.tsx
"use client";

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { api } from '@/lib/api';
import { LoginResponseDto } from '@/lib/types';
import Link from 'next/link';
import toast from 'react-hot-toast';
import { Zap } from 'lucide-react';
import { Button, Input } from '@/components/ui';

export default function LoginPage() {
  const router = useRouter();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    try {
      const response = await api.post<LoginResponseDto>('/users/login', { username, password });
      localStorage.setItem('token', response.data.token);
      toast.success('Bem-vindo de volta!');
      router.push('/');
    } catch {
      toast.error('Credenciais inválidas.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
      <div className="min-h-screen bg-zinc-950 flex items-center justify-center px-4">
        {/* Glow de fundo */}
        <div className="absolute inset-0 overflow-hidden pointer-events-none">
          <div className="absolute top-1/3 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[500px] h-[300px] bg-indigo-600/10 rounded-full blur-[100px]" />
        </div>

        <div className="relative w-full max-w-sm">
          {/* Logo */}
          <div className="flex flex-col items-center mb-8 gap-3">
            <div className="p-3 bg-indigo-500/10 border border-indigo-500/20 rounded-xl">
              <Zap size={22} className="text-indigo-400" />
            </div>
            <div className="text-center">
              <h1 className="text-xl font-semibold text-zinc-100 tracking-tight">shortener</h1>
              <p className="text-sm text-zinc-500 mt-0.5">Entre na sua conta</p>
            </div>
          </div>

          {/* Card */}
          <div className="bg-zinc-900 border border-zinc-800 rounded-2xl p-6 space-y-4">
            <form onSubmit={handleLogin} className="space-y-4">
              <Input
                  id="username"
                  label="Usuário"
                  type="text"
                  required
                  placeholder="seu_usuario"
                  autoComplete="username"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
              />
              <Input
                  id="password"
                  label="Senha"
                  type="password"
                  required
                  placeholder="••••••••"
                  autoComplete="current-password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
              />
              <Button type="submit" isLoading={isLoading} className="w-full mt-2" size="lg">
                Entrar
              </Button>
            </form>
          </div>

          <p className="mt-4 text-center text-sm text-zinc-600">
            Não tem conta?{' '}
            <Link href="/register" className="text-indigo-400 hover:text-indigo-300 transition-colors">
              Registre-se
            </Link>
          </p>
        </div>
      </div>
  );
}