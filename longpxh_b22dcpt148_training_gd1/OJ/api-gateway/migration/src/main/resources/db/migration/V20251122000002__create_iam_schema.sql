-- Create roles table
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Create permissions table
CREATE TABLE permissions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Create roles_permissions table (Many-to-Many)
CREATE TABLE roles_permissions (
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    permission_id BIGINT NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

-- Create users_roles table (Many-to-Many)
CREATE TABLE users_roles (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-- Insert default roles
INSERT INTO roles (name, description) VALUES 
('ADMIN', 'Administrator with full access'),
('LECTURER', 'Lecturer who can manage problems and contests'),
('STUDENT', 'Student who can submit solutions');

-- Insert default permissions
INSERT INTO permissions (name, description) VALUES
-- User Management
('USER_READ', 'Can view user details'),
('USER_WRITE', 'Can create/edit users'),
('USER_DELETE', 'Can delete users'),

-- Problem Management
('PROBLEM_READ', 'Can view problems'),
('PROBLEM_CREATE', 'Can create problems'),
('PROBLEM_EDIT', 'Can edit problems'),
('PROBLEM_DELETE', 'Can delete problems'),

-- Submission Management
('SUBMISSION_READ', 'Can view submissions'),
('SUBMISSION_CREATE', 'Can submit solutions'),

-- System
('SYSTEM_SETTINGS', 'Can manage system settings');

-- Assign permissions to roles

-- ADMIN gets all permissions
INSERT INTO roles_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'ADMIN';

-- LECTURER permissions
INSERT INTO roles_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r JOIN permissions p ON p.name IN (
    'USER_READ',
    'PROBLEM_READ', 'PROBLEM_CREATE', 'PROBLEM_EDIT', 'PROBLEM_DELETE',
    'SUBMISSION_READ'
)
WHERE r.name = 'LECTURER';

-- STUDENT permissions
INSERT INTO roles_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r JOIN permissions p ON p.name IN (
    'USER_READ',
    'PROBLEM_READ',
    'SUBMISSION_READ', 'SUBMISSION_CREATE'
)
WHERE r.name = 'STUDENT';

-- Migrate existing users to new roles structure
-- Assuming users.role contains values like 'ADMIN', 'STUDENT' matching roles.name
INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON u.role = r.name;

-- Drop the old role column
ALTER TABLE users DROP COLUMN role;
