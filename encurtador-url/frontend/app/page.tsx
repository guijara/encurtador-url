// src/app/page.tsx
"use client";

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { api } from '@/lib/api';
import { UrlResponseCompleteDto, PageResponse } from '@/lib/types';
import { formatDate } from '@/lib/utils';
import toast from 'react-hot-toast';
import {
  Copy, Trash2, Link as LinkIcon,
  ChevronLeft, ChevronRight, BarChart2, Globe, Zap, LogOut,
} from 'lucide-react';
import {
  Button, Input, Select, ExpirationBadge,
  SkeletonRow, Card, StatCard,
} from '@/components/ui';

export default function Dashboard() {
  const router = useRouter();
  const [urls, setUrls] = useState<UrlResponseCompleteDto[]>([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [totalElements, setTotalElements] = useState(0);
  const [newUrl, setNewUrl] = useState('');
  const [expirationType, setExpirationType] = useState('PERMANENT');
  const [isLoading, setIsLoading] = useState(false);
  const [isFetchingUrls, setIsFetchingUrls] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) { router.push('/login'); return; }
    fetchUrls(page);
  }, [page, router]);

  const fetchUrls = async (pageNumber: number) => {
    setIsFetchingUrls(true);
    try {
      const res = await api.get<PageResponse<UrlResponseCompleteDto>>(
          `/api/urls?page=${pageNumber}&size=10`
      );
      setUrls(res.data.content);
      setTotalPages(res.data.totalPages);
      setTotalElements(res.data.totalElements);
    } catch {
      toast.error('Falha ao carregar URLs.');
    } finally {
      setIsFetchingUrls(false);
    }
  };

  const handleShorten = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    try {
      await api.post('/api/urls', { url: newUrl, expirationType });
      toast.success('URL encurtada com sucesso!');
      setNewUrl('');
      setPage(0);
      fetchUrls(0);
    } catch {
      toast.error('Não foi possível encurtar a URL.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (!confirm('Tem a certeza que deseja remover esta URL?')) return;
    const toastId = toast.loading('Removendo...');
    try {
      await api.delete(`/api/urls/${id}`);
      toast.success('URL removida.', { id: toastId });
      fetchUrls(page);
    } catch {
      toast.error('Falha ao remover.', { id: toastId });
    }
  };

  const copyToClipboard = (shortUrl: string) => {
    navigator.clipboard.writeText(`http://localhost:8080/${shortUrl}`);
    toast.success('Copiado!', { icon: '📋', duration: 2000 });
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    router.push('/login');
  };

  const totalClicks = urls.reduce((sum, u) => sum + u.clicks, 0);

  return (
      <div className="min-h-screen bg-zinc-950 text-zinc-100">
        {/* ── TOP NAV ── */}
        <header className="border-b border-zinc-800/60 bg-zinc-950/80 backdrop-blur-sm sticky top-0 z-10">
          <div className="max-w-5xl mx-auto px-4 sm:px-6 h-14 flex items-center justify-between">
            <div className="flex items-center gap-2.5">
              <div className="p-1.5 bg-indigo-500/10 rounded-md">
                <Zap size={16} className="text-indigo-400" />
              </div>
              <span className="font-semibold text-sm text-zinc-100 tracking-tight">shortener</span>
            </div>
            <Button variant="ghost" size="sm" onClick={handleLogout}>
              <LogOut size={14} />
              Sair
            </Button>
          </div>
        </header>

        <main className="max-w-5xl mx-auto px-4 sm:px-6 py-8 space-y-6">

          {/* ── STAT CARDS ── */}
          <div className="grid grid-cols-2 sm:grid-cols-3 gap-3">
            <StatCard label="URLs criadas" value={totalElements} icon={<LinkIcon size={16} />} />
            <StatCard label="Cliques totais" value={totalClicks} icon={<BarChart2 size={16} />} />
            <StatCard
                label="Nesta página"
                value={urls.length}
                icon={<Globe size={16} />}
            />
          </div>

          {/* ── FORM ── */}
          <Card className="p-5">
            <p className="text-xs text-zinc-500 uppercase tracking-wider font-medium mb-4">
              Nova URL curta
            </p>
            <form onSubmit={handleShorten} className="flex flex-col sm:flex-row gap-3">
              <Input
                  type="url"
                  required
                  placeholder="https://exemplo.com/url-muito-longa-aqui"
                  value={newUrl}
                  onChange={(e) => setNewUrl(e.target.value)}
                  className="flex-1"
              />
              <Select
                  value={expirationType}
                  onChange={(e) => setExpirationType(e.target.value)}
              >
                <option value="PERMANENT">Permanente</option>
                <option value="SEVEN_DAYS">7 Dias</option>
                <option value="ONE_DAY">24 Horas</option>
              </Select>
              <Button type="submit" isLoading={isLoading} size="md" className="sm:self-end whitespace-nowrap">
                <Zap size={14} />
                Encurtar URL
              </Button>
            </form>
          </Card>

          {/* ── TABLE ── */}
          <Card>
            {/* Desktop table */}
            <div className="hidden sm:block overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                <tr className="border-b border-zinc-800">
                  <th className="px-4 py-3 text-left text-xs font-medium text-zinc-500 uppercase tracking-wider">
                    URL Original
                  </th>
                  <th className="px-4 py-3 text-left text-xs font-medium text-zinc-500 uppercase tracking-wider">
                    URL Curta
                  </th>
                  <th className="px-4 py-3 text-center text-xs font-medium text-zinc-500 uppercase tracking-wider">
                    Cliques
                  </th>
                  <th className="px-4 py-3 text-center text-xs font-medium text-zinc-500 uppercase tracking-wider">
                    Validade
                  </th>
                  <th className="px-4 py-3 text-center text-xs font-medium text-zinc-500 uppercase tracking-wider">
                    Criado em
                  </th>
                  <th className="px-4 py-3 text-right text-xs font-medium text-zinc-500 uppercase tracking-wider">
                    Ações
                  </th>
                </tr>
                </thead>
                <tbody>
                {isFetchingUrls
                    ? Array.from({ length: 5 }).map((_, i) => <SkeletonRow key={i} />)
                    : urls.length === 0
                        ? (
                            <tr>
                              <td colSpan={6} className="py-16 text-center text-zinc-600 text-sm">
                                Nenhuma URL criada ainda. Comece encurtando uma URL acima.
                              </td>
                            </tr>
                        )
                        : urls.map((url) => (
                            <tr
                                key={url.id}
                                className="border-b border-zinc-800/60 hover:bg-zinc-800/30 transition-colors group"
                            >
                              <td className="px-4 py-3.5 max-w-[220px] truncate text-zinc-400" title={url.originalUrl}>
                                {url.originalUrl}
                              </td>
                              <td className="px-4 py-3.5">
                                <a
                                    href={`http://localhost:8080/${url.shortUrl}`}
                                    target="_blank"
                                    rel="noreferrer"
                                    className="text-indigo-400 hover:text-indigo-300 font-mono text-xs font-medium transition-colors"
                                >
                                  /{url.shortUrl}
                                </a>
                              </td>
                              <td className="px-4 py-3.5 text-center">
                                <span className="font-medium text-zinc-300">{url.clicks}</span>
                              </td>
                              <td className="px-4 py-3.5 text-center">
                                <ExpirationBadge type={url.expirationType} />
                              </td>
                              <td className="px-4 py-3.5 text-center text-zinc-500 text-xs tabular-nums">
                                {formatDate(url.creationAt)}
                              </td>
                              <td className="px-4 py-3.5 text-right">
                                <div className="flex items-center justify-end gap-1 opacity-60 group-hover:opacity-100 transition-opacity">
                                  <Button
                                      variant="ghost"
                                      size="sm"
                                      onClick={() => copyToClipboard(url.shortUrl)}
                                      title="Copiar URL"
                                  >
                                    <Copy size={13} />
                                  </Button>
                                  <Button
                                      variant="danger"
                                      size="sm"
                                      onClick={() => handleDelete(url.id)}
                                      title="Remover"
                                  >
                                    <Trash2 size={13} />
                                  </Button>
                                </div>
                              </td>
                            </tr>
                        ))}
                </tbody>
              </table>
            </div>

            {/* Mobile cards */}
            <div className="sm:hidden divide-y divide-zinc-800/60">
              {isFetchingUrls
                  ? Array.from({ length: 3 }).map((_, i) => (
                      <div key={i} className="p-4 space-y-2.5">
                        <div className="h-3.5 bg-zinc-800 rounded animate-pulse w-3/4" />
                        <div className="h-3 bg-zinc-800 rounded animate-pulse w-1/2" />
                        <div className="h-3 bg-zinc-800 rounded animate-pulse w-1/3" />
                      </div>
                  ))
                  : urls.length === 0
                      ? (
                          <div className="py-16 text-center text-zinc-600 text-sm">
                            Nenhuma URL criada ainda.
                          </div>
                      )
                      : urls.map((url) => (
                          <div key={url.id} className="p-4 space-y-3">
                            <div className="flex items-start justify-between gap-2">
                              <p className="text-xs text-zinc-500 truncate flex-1" title={url.originalUrl}>
                                {url.originalUrl}
                              </p>
                              <div className="flex gap-1 shrink-0">
                                <Button variant="ghost" size="sm" onClick={() => copyToClipboard(url.shortUrl)}>
                                  <Copy size={13} />
                                </Button>
                                <Button variant="danger" size="sm" onClick={() => handleDelete(url.id)}>
                                  <Trash2 size={13} />
                                </Button>
                              </div>
                            </div>
                            <div className="flex items-center gap-3 flex-wrap">
                              <a
                                  href={`http://localhost:8080/${url.shortUrl}`}
                                  target="_blank"
                                  rel="noreferrer"
                                  className="text-indigo-400 font-mono text-xs"
                              >
                                /{url.shortUrl}
                              </a>
                              <ExpirationBadge type={url.expirationType} />
                              <span className="text-zinc-600 text-xs">{url.clicks} cliques</span>
                              <span className="text-zinc-600 text-xs">{formatDate(url.creationAt)}</span>
                            </div>
                          </div>
                      ))}
            </div>

            {/* Pagination */}
            {!isFetchingUrls && totalPages > 1 && (
                <div className="px-4 py-3 border-t border-zinc-800 flex items-center justify-between">
              <span className="text-xs text-zinc-600">
                Página {page + 1} de {totalPages}
              </span>
                  <div className="flex gap-2">
                    <Button
                        variant="ghost"
                        size="sm"
                        disabled={page === 0}
                        onClick={() => setPage((p) => Math.max(0, p - 1))}
                    >
                      <ChevronLeft size={14} />
                      Anterior
                    </Button>
                    <Button
                        variant="ghost"
                        size="sm"
                        disabled={page >= totalPages - 1}
                        onClick={() => setPage((p) => p + 1)}
                    >
                      Próximo
                      <ChevronRight size={14} />
                    </Button>
                  </div>
                </div>
            )}
          </Card>
        </main>
      </div>
  );
}