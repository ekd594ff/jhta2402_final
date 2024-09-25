import { Button, TextField } from "@mui/material";
import * as React from "react";
import axios from "axios";
import { useState } from "react";
import MenuItem from "@mui/material/MenuItem";

const isDeleted = [
  {
    value: true,
    label: "true",
  },
  {
    value: false,
    label: "false",
  },
];

function PortfolioModalContent(inputValue) {
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
        label="TITLE"
        defaultValue={inputValue.title}
        variant="standard"
        size="small"
        onChange={(event) => {
          const value = event.target.value;
          setValue((prev) => {
            return { ...prev, title: value };
          });
        }}
      />
      <TextField
        label="DESCRIPTION"
        defaultValue={inputValue.description}
        variant="standard"
        size="small"
        onChange={(event) => {
          const value = event.target.value;
          setValue((prev) => {
            return { ...prev, description: value };
          });
        }}
      />
      <TextField
        label="COMPANY-NAME"
        defaultValue={inputValue.companyName}
        variant="standard"
        size="small"
        slotProps={{
          input: {
            readOnly: true,
          },
        }}
      />
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
      <TextField
        label="DELETED"
        defaultValue={inputValue.deleted}
        select
        variant="standard"
        size="small"
        onChange={(event) => {
          const value = event.target.value;
          setValue((prev) => {
            return { ...prev, deleted: value };
          });
        }}
      >
        {isDeleted.map((option) => (
          <MenuItem key={option.value} value={option.value}>
            {option.label}
          </MenuItem>
        ))}
      </TextField>
      <TextField
        label="ACTIVATED"
        defaultValue={inputValue.activated}
        variant="standard"
        size="small"
        select
        onChange={(event) => {
          const value = event.target.value;
          setValue((prev) => {
            return { ...prev, activated: value };
          });
        }}
      >
        {isDeleted.map((option) => (
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
              const response = await axios.patch(`/api/portfolio/admin`, {
                id: value.id,
                title: value.title,
                description: value.description,
                deleted: value.deleted,
                activated: value.activated,
              });
              window.alert("포트폴리오 수정되었습니다");
              window.location.reload();
            } catch (err) {
              window.alert("포트폴리오 수정 실패");
            }
          }}
        >
          수정
        </Button>
      </div>
    </div>
  );
}

export default PortfolioModalContent;
