export interface NewNoteDto {
  title: string;
  content: string;
  imageName?: string;
  audioName?: string;
  tags?: string[];
}
