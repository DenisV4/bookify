import { useContext } from "react";
import { ConfirmationContext } from "../context/ConfirmationContext";

export const useConfirmation = () => useContext(ConfirmationContext)
