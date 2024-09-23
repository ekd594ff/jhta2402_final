import { Button, TextField } from "@mui/material";
import * as React from "react";
import { useState } from "react";
import axios from "axios";
import MenuItem from "@mui/material/MenuItem";

const progresses = [
  {
    value: "PENDING",
    label: "PENDING",
  },
  {
    value: "IN_PROGRESS",
    label: "IN_PROGRESS",
  },
  {
    value: "COMPLETED",
    label: "COMPLETED",
  },
];

function ReportModalContent(inputValue) {
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
        label="REF-TITLE"
        defaultValue={inputValue.refTitle}
        variant="standard"
        size="small"
        slotProps={{
          input: {
            readOnly: true,
          },
        }}
      />
      <TextField
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
        label="SORT"
        defaultValue={inputValue.sort}
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
        slotProps={{
          input: {
            readOnly: true,
          },
        }}
      />
      <TextField
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
        label="COMMENT"
        defaultValue={inputValue.comment}
        variant="standard"
        size="small"
        onChange={(event) => {
          const value = event.target.value;
          setValue((prev) => {
            return { ...prev, comment: value };
          });
        }}
      />
      <TextField
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
        {progresses.map((option) => (
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
            const response = await axios
              .patch(`/api/report/admin`, {
                id: value.id,
                comment: value.comment,
                progress: value.progress,
              })
              .catch((response) => console.log(response));
            console.log("role", value.role);
            console.log("value", value);
            console.log("event", event);
          }}
        >
          수정
        </Button>
      </div>
    </div>
  );
}
export default ReportModalContent;
