-- Users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'STUDENT',
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Problems table
CREATE TABLE problems (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    difficulty VARCHAR(20) NOT NULL,
    time_limit INTEGER NOT NULL DEFAULT 1000,
    memory_limit INTEGER NOT NULL DEFAULT 262144,
    created_by BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Test cases table
CREATE TABLE test_cases (
    id BIGSERIAL PRIMARY KEY,
    problem_id BIGINT NOT NULL REFERENCES problems(id) ON DELETE CASCADE,
    input TEXT NOT NULL,
    expected_output TEXT NOT NULL,
    is_sample BOOLEAN DEFAULT FALSE,
    points INTEGER DEFAULT 10,
    created_at TIMESTAMP DEFAULT NOW()
);

-- Submissions table
CREATE TABLE submissions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    problem_id BIGINT NOT NULL REFERENCES problems(id),
    code TEXT NOT NULL,
    language VARCHAR(20) NOT NULL DEFAULT 'cpp',
    verdict VARCHAR(20),
    status VARCHAR(20) DEFAULT 'PENDING',
    execution_time INTEGER,
    memory_used INTEGER,
    submitted_at TIMESTAMP DEFAULT NOW()
);

-- Submission results (per test case)
CREATE TABLE submission_results (
    id BIGSERIAL PRIMARY KEY,
    submission_id BIGINT NOT NULL REFERENCES submissions(id) ON DELETE CASCADE,
    test_case_id BIGINT NOT NULL REFERENCES test_cases(id),
    verdict VARCHAR(20),
    execution_time INTEGER,
    memory_used INTEGER,
    output TEXT,
    created_at TIMESTAMP DEFAULT NOW()
);

-- Indexes for performance
CREATE INDEX idx_submissions_user_id ON submissions(user_id);
CREATE INDEX idx_submissions_problem_id ON submissions(problem_id);
CREATE INDEX idx_submissions_status ON submissions(status);
CREATE INDEX idx_test_cases_problem_id ON test_cases(problem_id);
CREATE INDEX idx_submission_results_submission_id ON submission_results(submission_id);

-- Insert default admin user (password: admin123)
INSERT INTO users (username, email, password_hash, role) 
VALUES ('admin', 'admin@oj.com', '$2a$10$ZWC04/ZJdPKYDUj0nfqJa.UQ58VGzEsoFX1NIhVPmzjBReSxwfXGO', 'ADMIN');
