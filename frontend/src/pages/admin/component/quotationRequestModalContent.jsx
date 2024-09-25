import { Button, TextField } from "@mui/material";
import * as React from "react";
import { useState } from "react";
import axios from "axios";
import MenuItem from "@mui/material/MenuItem";

const quotationProgresses = [
  {
    value: "PENDING",
    label: "PENDING",
  },
  {
    value: "USER_CANCELLED",
    label: "USER_CANCELLED",
  },
  {
    value: "SELLER_CANCELLED",
    label: "SELLER_CANCELLED",
  },
  {
    value: "ADMIN_CANCELLED",
    label: "ADMIN_CANCELLED",
  },
  {
    value: "APPROVED",
    label: "APPROVED",
  },
];

function QuotationRequestModalContent(inputValue) {
  const { closer } = inputValue;
  const [value, setValue] = useState(inputValue);
  return (
    <div>
      <TextField
        id="filled-read-only-input"
        label="ID"
        defaultValue={inputValue.id}
        variant="standard"
        size="small"
        slotProps={{
          input: {
            readOnly: true,
          },
        }}
      />
      <TextField
        id="filled-read-only-input"
        label="USERNAME"
        defaultValue={inputValue.username}
        variant="standard"
        size="small"
        slotProps={{
          input: {
            readOnly: true,
          },
        }}
      />
      <TextField
        id="filled-read-only-input"
        label="PORTFOLIO-ID"
        defaultValue={inputValue.portfolioId}
        variant="standard"
        size="small"
        slotProps={{
          input: {
            readOnly: true,
          },
        }}
      />
      <TextField
        id="filled-read-only-input"
        label="TITLE"
        defaultValue={inputValue.title}
        variant="standard"
        size="small"
        slotProps={{
          input: {
            readOnly: true,
          },
        }}
      />
      <TextField
        id="filled-read-only-input"
        label="DESCRIPTION"
        defaultValue={inputValue.description}
        variant="standard"
        size="small"
        slotProps={{
          input: {
            readOnly: true,
          },
        }}
      />
      <TextField
        id="filled-read-only-input"
        label="PROGRESS"
        defaultValue={inputValue.progress}
        variant="standard"
        size="small"
        select
        onChange={(event) => {
          const value = event.target.value;
          setValue((prev) => {
            return { ...prev, progress: value };
          });
        }}
      >
        {quotationProgresses.map((option) => (
          <MenuItem key={option.value} value={option.value}>
            {option.label}
          </MenuItem>
        ))}
      </TextField>
      <TextField
        id="filled-read-only-input"
        label="CREATEDAT"
        defaultValue={inputValue.createdAt}
        variant="standard"
        size="small"
        slotProps={{
          input: {
            readOnly: true,
          },
        }}
      />
      <TextField
        id="filled-read-only-input"
        label="UPDATEDAT"
        defaultValue={inputValue.updatedAt}
        variant="standard"
        size="small"
        slotProps={{
          input: {
            readOnly: true,
          },
        }}
      />
      <div className="btn-group">
        <Button
          onClick={() => {
            closer();
          }}
        >
          닫기
        </Button>
        <Button
          onClick={async (event) => {
            try {
              const response = await axios.patch(
                `/api/quotationRequest/admin`,
                {
                  id: value.id,
                  progress: value.progress,
                }
              );
              window.alert("견적 신청서 수정되었습니다");
              window.location.reload();
            } catch (err) {
              window.alert("견적 신청서 수정 실패");
            }
          }}
        >
          수정
        </Button>
      </div>
    </div>
  );
}

export default QuotationRequestModalContent;
