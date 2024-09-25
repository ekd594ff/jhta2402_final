import { Button, TextField } from "@mui/material";
import * as React from "react";
import { useState } from "react";
import axios from "axios";
import MenuItem from "@mui/material/MenuItem";

const isApplied = [
  {
    value: true,
    label: "true",
  },
  {
    value: false,
    label: "false",
  },
];

function CompanyModalContent(inputValue) {
  const { closer } = inputValue;
  const [value, setValue] = useState(inputValue);
  return (
    <div>
      <TextField
        label="ID"
        size="small"
        defaultValue={inputValue.id}
        variant="standard"
        slotProps={{
          input: {
            readOnly: true,
          },
        }}
      />
      <TextField
        label="COMPANY-NAME"
        size="small"
        defaultValue={inputValue.companyName}
        variant="standard"
        onChange={(event) => {
          const value = event.target.value;
          setValue((prev) => {
            return { ...prev, companyName: value };
          });
        }}
      />
      <TextField
        label="DESCRIPTION"
        size="small"
        defaultValue={inputValue.description}
        variant="standard"
        onChange={(event) => {
          const value = event.target.value;
          setValue((prev) => {
            return { ...prev, description: value };
          });
        }}
      />
      <TextField
        label="PHONE"
        size="small"
        defaultValue={inputValue.phone}
        variant="standard"
        onChange={(event) => {
          const value = event.target.value;
          setValue((prev) => {
            return { ...prev, phone: value };
          });
        }}
      />
      <TextField
        label="ADDRESS"
        size="small"
        defaultValue={inputValue.address}
        variant="standard"
        onChange={(event) => {
          const value = event.target.value;
          setValue((prev) => {
            return { ...prev, address: value };
          });
        }}
      />
      <TextField
        size="small"
        label="DETAIL-ADDRESS"
        defaultValue={inputValue.detailAddress}
        variant="standard"
        onChange={(event) => {
          const value = event.target.value;
          setValue((prev) => {
            return { ...prev, detailAddress: value };
          });
        }}
      />
      <TextField
        size="small"
        label="CREATEDAT"
        defaultValue={inputValue.createdAt}
        variant="standard"
        slotProps={{
          input: {
            readOnly: true,
          },
        }}
      />
      <TextField
        label="APPLIED"
        size="small"
        defaultValue={inputValue.applied}
        select
        onChange={(event) => {
          const value = event.target.value;
          setValue((prev) => {
            return { ...prev, applied: value };
          });
        }}
      >
        {isApplied.map((option) => (
          <MenuItem key={option.value} value={option.value}>
            {option.label}
          </MenuItem>
        ))}
      </TextField>
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
              const response = await axios.patch(`/api/company/admin`, {
                id: value.id,
                companyName: value.companyName,
                description: value.description,
                phone: value.phone,
                address: value.address,
                detailAddress: value.detailAddress,
                applied: value.applied,
              });
              window.alert("업체정보 수정되었습니다");
              window.location.reload();
            } catch (err) {
              window.alert("업체정보 수정 실패");
            }
          }}
        >
          수정
        </Button>
      </div>
    </div>
  );
}

export default CompanyModalContent;
