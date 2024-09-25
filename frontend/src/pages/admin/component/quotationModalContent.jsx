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

function QuotationModalContent(inputValue) {
  const { closer } = inputValue;
  const [value, setValue] = useState(inputValue);
  return (
    <div>
      <TextField
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
        label="TOTAL-TRANSACTION-AMOUNT"
        defaultValue={inputValue.totalTransactionAmount}
        variant="standard"
        size="small"
        slotProps={{
          input: {
            readOnly: true,
          },
        }}
      />
      <TextField
        label="PROGRESS"
        defaultValue={inputValue.progress}
        select
        variant="standard"
        size="small"
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
              const response = await axios.patch(`/api/quotation/admin`, {
                id: value.id,
                progress: value.progress,
              });
              window.alert("견적서 수정되었습니다");
              window.location.reload();
            } catch (err) {
              window.alert("견적서 수정 실패");
            }
          }}
        >
          수정
        </Button>
      </div>
    </div>
  );
}

export default QuotationModalContent;
