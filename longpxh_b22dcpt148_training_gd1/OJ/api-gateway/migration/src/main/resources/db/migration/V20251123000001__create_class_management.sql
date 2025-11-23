-- Classes table
CREATE TABLE classes (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    semester VARCHAR(20),
    academic_year VARCHAR(20),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Teacher-Class relationship (teachers can manage multiple classes)
CREATE TABLE teacher_classes (
    id BIGSERIAL PRIMARY KEY,
    teacher_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    class_id BIGINT NOT NULL REFERENCES classes(id) ON DELETE CASCADE,
    assigned_at TIMESTAMP DEFAULT NOW(),
    UNIQUE(teacher_id, class_id)
);

-- Student-Class relationship (students belong to classes)
CREATE TABLE student_classes (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    class_id BIGINT NOT NULL REFERENCES classes(id) ON DELETE CASCADE,
    enrolled_at TIMESTAMP DEFAULT NOW(),
    UNIQUE(student_id, class_id)
);

-- Add class_id to problems table (problems belong to classes)
ALTER TABLE problems ADD COLUMN class_id BIGINT REFERENCES classes(id) ON DELETE CASCADE;

-- Indexes for performance
CREATE INDEX idx_teacher_classes_teacher_id ON teacher_classes(teacher_id);
CREATE INDEX idx_teacher_classes_class_id ON teacher_classes(class_id);
CREATE INDEX idx_student_classes_student_id ON student_classes(student_id);
CREATE INDEX idx_student_classes_class_id ON student_classes(class_id);
CREATE INDEX idx_problems_class_id ON problems(class_id);
