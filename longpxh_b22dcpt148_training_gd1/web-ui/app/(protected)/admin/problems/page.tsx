"use client"

import {
    Alert,
    Button,
    Container,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle, Snackbar, SnackbarCloseReason, TextareaAutosize, TextField,
} from "@mui/material";
import { api } from "@/lib/api";
import Title from "@/components/title/Title";
import { ProblemFilter } from "@/components/filter/ProblemFilter";
import Table, { Column } from "@/components/table/Table";
import { useRouter } from "next/navigation";
import BreadCumb from "@/components/breadcumb/BreadCumb";
import React, { useState } from "react";
import CheckIcon from '@mui/icons-material/Check';
import Select from "@/components/select/Select";
import { MuiFileInput } from "mui-file-input";


const columns: Column[] = [
    { label: "Mã bài", key: ["code"] },
    { label: "Tên bài", key: ["title"] },
    { label: "Độ khó", key: ["level"] },
    { label: "Trạng thái", key: ["status"] }
];


interface ProblemFilter {
    code: string,
    title: string,
    level: string
}

const ProblemFilterDefault = {
    code: '',
    title: '',
    level: ''
}


const inherit = [
    {
        labelInherit: "Admin Dashboard",
        hrefInherit: "/admin"
    }
]

const primary = {
    labelPrimary: `Quản lý bài tập`,
    hrefPrimary: `/admin/problems} `
}

const ProblemSelect = [
    {
        value: "EASY",
        label: "Dễ"
    }, {
        value: "MEDIUM",
        label: "Trung bình"
    }, {
        value: "HARD",
        label: "Khó"
    },
]

export default function AdminProblems() {

    const [rows, setRows] = useState<any[]>([]);
    const [open, setOpen] = React.useState(false);
    const [openSnack, setOpenSnack] = React.useState(false)
    const [filterState, setFilterState] = useState<ProblemFilter>(ProblemFilterDefault);
    const [showError, setShowError] = React.useState(false);
    const [value, setValue] = useState<File | null>(null);

    const fetchProblems = async () => {
        try {
            const queryParams = new URLSearchParams();
            if (filterState.code) queryParams.append('code', filterState.code);
            if (filterState.title) queryParams.append('title', filterState.title);
            if (filterState.level) queryParams.append('level', filterState.level);

            const response: any = await api(`/api/problems?${queryParams.toString()}`);


            const formattedRows = response.map((p: any) => ({
                id: p.id,
                data: {
                    code: p.problemCode,
                    title: p.title,
                    level: p.level,
                    status: "Hoạt động"
                }
            }));
            setRows(formattedRows);
        } catch (error) {
            console.error("Failed to fetch problems", error);
        }
    };

    React.useEffect(() => {
        fetchProblems();
    }, [filterState]);


    const titleRef = React.useRef<HTMLInputElement>(null);

    const handleClickOpen = () => {
        setShowError(false);
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
        setShowError(false);
    };

    const handleAdd = async () => {
        const title = titleRef.current?.value || '';
        const level = filterState.level;
        const file = value;

        if (!title.trim() || !level || !file) {
            setShowError(true);
            return;
        }

        try {
            const formData = new FormData();
            const problemData = {
                title,
                content: "From Zip",
                level,
                timeLimit: 1,
                memoryLimit: 1,
                classId: null
            };
            formData.append("data", new Blob([JSON.stringify(problemData)], { type: "application/json" }));
            formData.append("file", file);

            await api('/api/problems', {
                method: 'POST',
                body: formData
            });

            setShowError(false);
            setOpenSnack(true);
            await fetchProblems(); // Refresh list
            handleClose();
        } catch (error: any) {
            console.error(error);
            alert("Thêm bài tập thất bại: " + error.message);
        }
    }

    const handleCloseSnack = (
        event?: React.SyntheticEvent | Event,
        reason?: SnackbarCloseReason,
    ) => {
        if (reason === 'clickaway') {
            return;
        }

        setOpenSnack(false);
    };

    const handleChangeTextField = (value: string, name: string) => {
        setFilterState((prev) => (
            {
                ...prev,
                [name]: value,
            }
        ))
    }

    const handleChange = (file: File | null) => {
        if (!file) return;

        const allowed = ["zip"];
        const ext = file.name.split(".").pop()?.toLowerCase();

        if (!ext || !allowed.includes(ext)) {
            alert("Chỉ cho phép file .zip chứa testcase");
            setValue(null);
            return;
        }

        setValue(file);
    };


    return (
        <Container>
            <BreadCumb inherit={inherit} primary={primary} />
            <div className="flex justify-between">
                <Title titlePage="Quản lý bài tập" />
                <Button onClick={handleClickOpen} variant="contained">Thêm bài tập</Button>
            </div>
            <ProblemFilter />
            <Table
                columns={columns}
                rows={rows}
            />

            <Dialog
                open={open}
                onClose={handleClose}
            >
                <DialogTitle>
                    Thêm bài tập
                </DialogTitle>
                <DialogContent>
                    {showError && (
                        <Alert severity="error" className="!mb-3">
                            Vui lòng điền đủ các trường!
                        </Alert>
                    )}
                    <DialogContentText id="alert-dialog-description">
                        <TextField label="Tên bài" fullWidth className="!mb-3" inputRef={titleRef} />

                        <Select
                            value={filterState.level}
                            label="Chọn độ khó"
                            options={ProblemSelect}
                            onChange={(value) => handleChangeTextField(value, 'level')} />

                        <MuiFileInput fullWidth label="Tải file testcase (.zip chứa .xml/.in/.out)"
                            value={value}
                            inputProps={{
                                accept: ".zip"
                            }}
                            onChange={handleChange} />
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleAdd}>Thêm</Button>
                    <Button onClick={handleClose} autoFocus>
                        Hủy
                    </Button>
                </DialogActions>
            </Dialog>

            <Snackbar open={openSnack} autoHideDuration={6000} onClose={handleCloseSnack}>
                <Alert
                    onClose={handleCloseSnack}
                    severity="success"
                    variant="filled"
                    sx={{ width: '100%' }}
                >
                    Thêm bài tập thành công!
                </Alert>
            </Snackbar>
        </Container >
    )
}