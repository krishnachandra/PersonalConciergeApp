# Davinci — Supabase Database Setup

This SQL script creates the full database schema for Davinci.
Run this in your Supabase SQL Editor (Dashboard → SQL Editor → New Query).

## Setup Instructions

1. Go to https://supabase.com and create a new project
2. Open the SQL Editor
3. Run the schema below
4. Update `app/build.gradle.kts` with your:
   - `SUPABASE_URL` → Project Settings → API → URL
   - `SUPABASE_ANON_KEY` → Project Settings → API → anon public key

---

```sql
-- ============================================================
-- DAVINCI DATABASE SCHEMA
-- ============================================================

-- Enable UUID generation
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ─── USERS ──────────────────────────────────────────────────
-- Extends Supabase auth.users with app-specific profile data
CREATE TABLE IF NOT EXISTS public.profiles (
    id UUID REFERENCES auth.users(id) ON DELETE CASCADE PRIMARY KEY,
    email TEXT NOT NULL,
    display_name TEXT NOT NULL DEFAULT 'User',
    avatar_url TEXT,
    primary_timezone TEXT DEFAULT 'America/New_York',
    secondary_timezone TEXT DEFAULT 'Asia/Kolkata',
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- ─── FAMILIES ───────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS public.families (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    name TEXT NOT NULL DEFAULT 'My Family',
    created_by UUID REFERENCES public.profiles(id),
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- ─── FAMILY MEMBERS ─────────────────────────────────────────
CREATE TABLE IF NOT EXISTS public.family_members (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    family_id UUID REFERENCES public.families(id) ON DELETE CASCADE NOT NULL,
    user_id UUID REFERENCES public.profiles(id) ON DELETE CASCADE NOT NULL,
    role TEXT CHECK (role IN ('admin', 'member')) DEFAULT 'member',
    joined_at TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(family_id, user_id)
);

-- ─── TASKS ──────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS public.tasks (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    family_id UUID REFERENCES public.families(id) ON DELETE CASCADE NOT NULL,
    title TEXT NOT NULL,
    notes TEXT,
    category TEXT CHECK (category IN ('admin', 'groceries', 'finance', 'travel', 'home', 'family')) DEFAULT 'admin',
    assigned_to UUID REFERENCES public.profiles(id),
    created_by UUID REFERENCES public.profiles(id) NOT NULL,
    is_urgent BOOLEAN DEFAULT FALSE,
    status TEXT CHECK (status IN ('pending', 'completed')) DEFAULT 'pending',
    due_date TIMESTAMPTZ,
    completed_at TIMESTAMPTZ,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- ─── TASK REMINDERS ─────────────────────────────────────────
CREATE TABLE IF NOT EXISTS public.task_reminders (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    task_id UUID REFERENCES public.tasks(id) ON DELETE CASCADE NOT NULL,
    remind_at TIMESTAMPTZ NOT NULL,
    sent BOOLEAN DEFAULT FALSE
);

-- ─── EVENTS ─────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS public.events (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    family_id UUID REFERENCES public.families(id) ON DELETE CASCADE NOT NULL,
    title TEXT NOT NULL,
    type TEXT CHECK (type IN ('birthday', 'event', 'appointment')) DEFAULT 'event',
    recurrence TEXT CHECK (recurrence IN ('none', 'yearly', 'monthly', 'weekly')) DEFAULT 'none',
    event_date DATE NOT NULL,
    event_time TIME,
    created_by UUID REFERENCES public.profiles(id) NOT NULL,
    metadata JSONB DEFAULT '{}'::jsonb,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- ─── EVENT REMINDERS ────────────────────────────────────────
CREATE TABLE IF NOT EXISTS public.event_reminders (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    event_id UUID REFERENCES public.events(id) ON DELETE CASCADE NOT NULL,
    remind_before TEXT CHECK (remind_before IN ('1_hour', '1_day', '1_week')) DEFAULT '1_day',
    sent BOOLEAN DEFAULT FALSE,
    remind_at TIMESTAMPTZ NOT NULL
);

-- ─── DEVICE TOKENS ──────────────────────────────────────────
CREATE TABLE IF NOT EXISTS public.device_tokens (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    user_id UUID REFERENCES public.profiles(id) ON DELETE CASCADE NOT NULL,
    fcm_token TEXT NOT NULL UNIQUE,
    device_name TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    last_active TIMESTAMPTZ DEFAULT NOW()
);

-- ─── NOTIFICATION PREFERENCES ───────────────────────────────
CREATE TABLE IF NOT EXISTS public.notification_preferences (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    user_id UUID REFERENCES public.profiles(id) ON DELETE CASCADE UNIQUE NOT NULL,
    tasks_enabled BOOLEAN DEFAULT TRUE,
    events_enabled BOOLEAN DEFAULT TRUE,
    market_enabled BOOLEAN DEFAULT TRUE,
    quiet_start TIME DEFAULT '22:00',
    quiet_end TIME DEFAULT '07:00'
);

-- ─── INVESTMENT CACHE ───────────────────────────────────────
CREATE TABLE IF NOT EXISTS public.investment_cache (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    metal TEXT CHECK (metal IN ('gold', 'silver')) NOT NULL,
    price_usd DECIMAL(10, 2) NOT NULL,
    change_24h DECIMAL(5, 2),
    direction TEXT CHECK (direction IN ('up', 'down')),
    fetched_at TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(metal)
);

-- ─── NEWS CACHE ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS public.news_cache (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    source TEXT NOT NULL,
    headline TEXT NOT NULL,
    summary TEXT,
    url TEXT,
    published_at TIMESTAMPTZ,
    fetched_at TIMESTAMPTZ DEFAULT NOW()
);

-- ============================================================
-- INDEXES
-- ============================================================
CREATE INDEX IF NOT EXISTS idx_tasks_family_status ON tasks(family_id, status) WHERE is_deleted = FALSE;
CREATE INDEX IF NOT EXISTS idx_tasks_assigned_due ON tasks(assigned_to, due_date) WHERE status = 'pending';
CREATE INDEX IF NOT EXISTS idx_tasks_urgent ON tasks(family_id, is_urgent, due_date) WHERE status = 'pending' AND is_urgent = TRUE;
CREATE INDEX IF NOT EXISTS idx_events_family_date ON events(family_id, event_date);
CREATE INDEX IF NOT EXISTS idx_task_reminders_pending ON task_reminders(remind_at) WHERE sent = FALSE;
CREATE INDEX IF NOT EXISTS idx_investment_cache ON investment_cache(metal);

-- ============================================================
-- ROW LEVEL SECURITY (RLS)
-- ============================================================
ALTER TABLE public.profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.families ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.family_members ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.tasks ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.events ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.device_tokens ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.notification_preferences ENABLE ROW LEVEL SECURITY;

-- Profiles: users can read/update their own profile
CREATE POLICY "profiles_select_own" ON public.profiles
    FOR SELECT USING (id = auth.uid());
CREATE POLICY "profiles_update_own" ON public.profiles
    FOR UPDATE USING (id = auth.uid());

-- Family members: users can see their own family
CREATE POLICY "family_members_select" ON public.family_members
    FOR SELECT USING (
        family_id IN (SELECT family_id FROM family_members WHERE user_id = auth.uid())
    );

-- Tasks: family-scoped access
CREATE POLICY "tasks_select" ON public.tasks
    FOR SELECT USING (
        family_id IN (SELECT family_id FROM family_members WHERE user_id = auth.uid())
    );
CREATE POLICY "tasks_insert" ON public.tasks
    FOR INSERT WITH CHECK (
        family_id IN (SELECT family_id FROM family_members WHERE user_id = auth.uid())
    );
CREATE POLICY "tasks_update" ON public.tasks
    FOR UPDATE USING (
        assigned_to = auth.uid()
        OR EXISTS (
            SELECT 1 FROM family_members
            WHERE user_id = auth.uid() AND family_id = tasks.family_id AND role = 'admin'
        )
    );
CREATE POLICY "tasks_delete" ON public.tasks
    FOR DELETE USING (
        EXISTS (
            SELECT 1 FROM family_members
            WHERE user_id = auth.uid() AND family_id = tasks.family_id AND role = 'admin'
        )
    );

-- Events: family-scoped
CREATE POLICY "events_select" ON public.events
    FOR SELECT USING (
        family_id IN (SELECT family_id FROM family_members WHERE user_id = auth.uid())
    );

-- Device tokens: own tokens only
CREATE POLICY "device_tokens_own" ON public.device_tokens
    FOR ALL USING (user_id = auth.uid());

-- Notification preferences: own prefs only
CREATE POLICY "notif_prefs_own" ON public.notification_preferences
    FOR ALL USING (user_id = auth.uid());

-- ============================================================
-- TRIGGER: Auto-create profile on signup
-- ============================================================
CREATE OR REPLACE FUNCTION public.handle_new_user()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO public.profiles (id, email, display_name)
    VALUES (
        NEW.id,
        NEW.email,
        COALESCE(NEW.raw_user_meta_data->>'display_name', split_part(NEW.email, '@', 1))
    );
    RETURN NEW;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

CREATE OR REPLACE TRIGGER on_auth_user_created
    AFTER INSERT ON auth.users
    FOR EACH ROW EXECUTE FUNCTION public.handle_new_user();

-- ============================================================
-- REALTIME: Enable for tasks (live sync)
-- ============================================================
ALTER PUBLICATION supabase_realtime ADD TABLE public.tasks;
```
