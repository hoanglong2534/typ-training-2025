"use client";


import Table, { Column } from "@/components/table/Table";
import { Container } from "@mui/material";
import Title from "@/components/title/Title";
import { ProblemFilter as ProblemFilter } from "@/components/filter/ProblemFilter";
import { useRouter } from "next/navigation";
import { Property } from "csstype";
import { useState, useEffect } from "react";
import { api } from "@/lib/api";
import Columns = Property.Columns;


const columns: Column[] = [
    { label: "Mã bài", key: ["code"] },
    { label: "Tên bài", key: ["title"] },
    { label: "Độ khó", key: ["level"] },
    { label: "Trạng thái", key: ["status"] }
];


export default function Problems() {

    const router = useRouter();
    const [rows, setRows] = useState<any[]>([]);

    useEffect(() => {
        const fetchProblems = async () => {
            try {
                const response: any = await api('/api/problems');
                const formattedRows = response.map((p: any) => ({
                    id: p.id,
                    data: {
                        code: p.problemCode,
                        title: p.title,
                        level: p.level,
                        status: "Chưa làm"
                    }
                }));
                setRows(formattedRows);
            } catch (error) {
                console.error("Failed to fetch problems", error);
            }
        };
        fetchProblems();
    }, []);

    return (
        <Container>
            <Title titlePage="Danh sách bài tập" />
            <ProblemFilter />
            <Table
                columns={columns}
                rows={rows}
                onClick={(id: number) => router.push(`problems/${id}`)}
            />

        </Container>
    )
}