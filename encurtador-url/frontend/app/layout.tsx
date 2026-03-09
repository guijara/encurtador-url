// src/app/layout.tsx
import type { Metadata } from 'next';
import { Geist, Geist_Mono } from 'next/font/google';
import './globals.css';
import { Toaster } from 'react-hot-toast';

const geistSans = Geist({
    variable: '--font-geist-sans',
    subsets: ['latin'],
});

const geistMono = Geist_Mono({
    variable: '--font-geist-mono',
    subsets: ['latin'],
});

export const metadata: Metadata = {
    title: 'Encurtador - Guilherme',
    description: 'Encurtador de URLs moderno com Spring Boot',
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
    return (
        <html lang="pt" className="dark">
        <body className={`${geistSans.variable} ${geistMono.variable} antialiased`}>
        <Toaster
            position="top-right"
            toastOptions={{
                style: {
                    background: '#18181b',
                    color: '#f4f4f5',
                    border: '1px solid #27272a',
                    borderRadius: '10px',
                    fontSize: '14px',
                    padding: '10px 14px',
                },
                success: {
                    iconTheme: { primary: '#818cf8', secondary: '#18181b' },
                },
                error: {
                    iconTheme: { primary: '#fb7185', secondary: '#18181b' },
                },
            }}
        />
        {children}
        </body>
        </html>
    );
}