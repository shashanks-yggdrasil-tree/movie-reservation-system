import {
  useCreateTheaterMutation,
  useGetAllTheatersQuery,
} from "../services/userApi";
import { CustomBtn } from "../shared/components/CustomBtn";
import LinkBtn from "../shared/components/LinkBtn";
import { generateRandomTheater } from "../shared/functions";
import JSONPretty from 'react-json-pretty';

function Home() {
  const { data: theaters, refetch } = useGetAllTheatersQuery();
  const [triggerCreateTheater, { data }] = useCreateTheaterMutation();

  const createTheater = async () => {
    const randomTheater = generateRandomTheater();
    try {
      const res = await triggerCreateTheater(randomTheater).unwrap();
      console.log("res", res);
      refetch();
    } catch (e: any) {
      console.log(e);
    }
  };

  return (
    <div className="h-full">
      <div className="grid gap-4 p-4 ">
        <LinkBtn to="/screen-saver" title="Go to Screen Saver Page" />

        <JSONPretty id="json-pretty" data={theaters}></JSONPretty>
        <CustomBtn type="button" onClick={createTheater}>
          Create a New Theater
        </CustomBtn>
      </div>
    </div>
  );
}

export default Home;
