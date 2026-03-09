// src/components/ui/index.tsx
"use client";

import { forwardRef, ButtonHTMLAttributes, InputHTMLAttributes, SelectHTMLAttributes } from 'react';
import { Loader2 } from 'lucide-react';
import { cn } from '@/lib/utils';

// ─── BUTTON ────────────────────────────────────────────────────────────────────
interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'ghost' | 'danger';
  size?: 'sm' | 'md' | 'lg';
  isLoading?: boolean;
}

export const Button = forwardRef<HTMLButtonElement, ButtonProps>(
    ({ className, variant = 'primary', size = 'md', isLoading, disabled, children, ...props }, ref) => {
      const base =
          'inline-flex items-center justify-center font-medium rounded-lg transition-all duration-200 focus:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 focus-visible:ring-offset-zinc-950 active:scale-[0.98] disabled:pointer-events-none disabled:opacity-40 select-none';

      const variants = {
        primary:
            'bg-indigo-600 text-white hover:bg-indigo-500 focus-visible:ring-indigo-500 shadow-lg shadow-indigo-500/20 hover:shadow-indigo-500/40',
        ghost:
            'bg-transparent text-zinc-400 border border-zinc-800 hover:border-zinc-600 hover:text-zinc-200 focus-visible:ring-zinc-500',
        danger:
            'bg-transparent text-zinc-500 hover:text-red-400 hover:bg-red-500/10 focus-visible:ring-red-500',
      };

      const sizes = {
        sm: 'text-xs px-3 py-1.5 gap-1.5',
        md: 'text-sm px-4 py-2.5 gap-2',
        lg: 'text-sm px-6 py-3 gap-2',
      };

      return (
          <button
              ref={ref}
              disabled={isLoading || disabled}
              className={cn(base, variants[variant], sizes[size], className)}
              {...props}
          >
            {isLoading && <Loader2 size={14} className="animate-spin" />}
            {children}
          </button>
      );
    }
);
Button.displayName = 'Button';

// ─── INPUT ─────────────────────────────────────────────────────────────────────
interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  error?: string;
}

export const Input = forwardRef<HTMLInputElement, InputProps>(
    ({ className, label, error, id, ...props }, ref) => (
        <div className="flex flex-col gap-1.5 w-full">
          {label && (
              <label htmlFor={id} className="text-xs font-medium text-zinc-400 uppercase tracking-wider">
                {label}
              </label>
          )}
          <input
              ref={ref}
              id={id}
              className={cn(
                  'w-full bg-zinc-900 border border-zinc-800 text-zinc-100 placeholder-zinc-600',
                  'rounded-lg px-3.5 py-2.5 text-sm',
                  'transition-all duration-200',
                  'focus:outline-none focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500/50',
                  'hover:border-zinc-700',
                  error && 'border-red-500/60 focus:border-red-500 focus:ring-red-500/30',
                  className
              )}
              {...props}
          />
          {error && <p className="text-xs text-red-400">{error}</p>}
        </div>
    )
);
Input.displayName = 'Input';

// ─── SELECT ────────────────────────────────────────────────────────────────────
interface SelectProps extends SelectHTMLAttributes<HTMLSelectElement> {
  label?: string;
}

export const Select = forwardRef<HTMLSelectElement, SelectProps>(
    ({ className, label, id, children, ...props }, ref) => (
        <div className="flex flex-col gap-1.5">
          {label && (
              <label htmlFor={id} className="text-xs font-medium text-zinc-400 uppercase tracking-wider">
                {label}
              </label>
          )}
          <select
              ref={ref}
              id={id}
              className={cn(
                  'bg-zinc-900 border border-zinc-800 text-zinc-100',
                  'rounded-lg px-3.5 py-2.5 text-sm',
                  'transition-all duration-200 cursor-pointer',
                  'focus:outline-none focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500/50',
                  'hover:border-zinc-700',
                  className
              )}
              {...props}
          >
            {children}
          </select>
        </div>
    )
);
Select.displayName = 'Select';

// ─── BADGE ─────────────────────────────────────────────────────────────────────
type BadgeVariant = 'PERMANENT' | 'SEVEN_DAYS' | 'ONE_DAY';

const badgeConfig: Record<BadgeVariant, { label: string; className: string }> = {
  PERMANENT: {
    label: 'Permanente',
    className: 'bg-emerald-500/10 text-emerald-400 border-emerald-500/20',
  },
  SEVEN_DAYS: {
    label: '7 Dias',
    className: 'bg-amber-500/10 text-amber-400 border-amber-500/20',
  },
  ONE_DAY: {
    label: '24 Horas',
    className: 'bg-rose-500/10 text-rose-400 border-rose-500/20',
  },
};

export function ExpirationBadge({ type }: { type: string }) {
  const config = badgeConfig[type as BadgeVariant] ?? {
    label: type,
    className: 'bg-zinc-500/10 text-zinc-400 border-zinc-500/20',
  };

  return (
      <span
          className={cn(
              'inline-flex items-center px-2 py-0.5 rounded-md text-xs font-medium border',
              config.className
          )}
      >
      {config.label}
    </span>
  );
}

// ─── SKELETON ──────────────────────────────────────────────────────────────────
function SkeletonCell({ className }: { className?: string }) {
  return (
      <div className={cn('h-4 bg-zinc-800 rounded animate-pulse', className)} />
  );
}

export function SkeletonRow() {
  return (
      <tr className="border-b border-zinc-800/60">
        <td className="p-4"><SkeletonCell className="w-3/4" /></td>
        <td className="p-4"><SkeletonCell className="w-20" /></td>
        <td className="p-4"><SkeletonCell className="w-16 mx-auto" /></td>
        <td className="p-4"><SkeletonCell className="w-24 mx-auto" /></td>
        <td className="p-4"><SkeletonCell className="w-20 ml-auto" /></td>
      </tr>
  );
}

// ─── CARD ──────────────────────────────────────────────────────────────────────
export function Card({ className, children }: { className?: string; children: React.ReactNode }) {
  return (
      <div
          className={cn(
              'bg-zinc-900 border border-zinc-800 rounded-xl overflow-hidden',
              className
          )}
      >
        {children}
      </div>
  );
}

// ─── STAT CARD ─────────────────────────────────────────────────────────────────
export function StatCard({
                           label,
                           value,
                           icon,
                         }: {
  label: string;
  value: string | number;
  icon: React.ReactNode;
}) {
  return (
      <div className="bg-zinc-900 border border-zinc-800 rounded-xl p-4 flex items-center gap-4">
        <div className="p-2.5 bg-indigo-500/10 rounded-lg text-indigo-400">{icon}</div>
        <div>
          <p className="text-xs text-zinc-500 font-medium uppercase tracking-wider">{label}</p>
          <p className="text-2xl font-bold text-zinc-100 mt-0.5">{value}</p>
        </div>
      </div>
  );
}