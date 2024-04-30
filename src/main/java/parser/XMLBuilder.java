package parser;

import java.math.BigDecimal;

import javax.xml.stream.XMLStreamReader;

import parser.TagAddress.AddressBuilder;
import parser.TagRelation.RelationBuilder;
import parser.TagWay.WayBuilder;
import util.MecatorProjection;

/**
* Builder for a single XML element.
* <p>
* Constructs a instance of the builder, that later can be used to construct a {@link TagNode}, {@link TagWay} or {@link TagRelation}.
* </p>
*/
public class XMLBuilder {
        private AddressBuilder addressBuilder = new AddressBuilder();
        private WayBuilder wayBuilder = new WayBuilder();
        private RelationBuilder relationBuilder = new RelationBuilder();

        private String name; // name from a <tag> in a parrent element
        private Type type;
        private String TypeValue;
        private long id;
        private float lat, lon;

        /**
         * Get a attrubute from the {@link XMLStreamReader} as a {@link BigDecimal}.
         * @param event - The {@link XMLStreamReader} to get the attribute from.
         * @param name - The name of the attribute to get. ({@link String})
         * @return The attribute as a {@link BigDecimal}.
         */
        public static double getAttributeByDouble(XMLStreamReader event, String name) {
            return Double.parseDouble(event.getAttributeValue(null, name));
        }

                /**
         * Get a attrubute from the {@link XMLStreamReader} as a {@link BigDecimal}.
         * @param event - The {@link XMLStreamReader} to get the attribute from.
         * @param name - The name of the attribute to get. ({@link String})
         * @return The attribute as a {@link BigDecimal}.
         */
        public static float getAttributeByFloat(XMLStreamReader event, String name) {
            return Float.parseFloat(event.getAttributeValue(null, name));
        }
        
        /**
         * Get a attrubute from the {@link XMLStreamReader} as a {@link Long}.
         * @param event - The {@link XMLStreamReader} to get the attribute from.
         * @param name - The name of the attribute to get. ({@link String})
         * @return The attribute as a {@link Long}.
         */
        public static Long getAttributeByLong(XMLStreamReader event, String name) {
            return Long.parseUnsignedLong(event.getAttributeValue(null, name));
        }
        
        public boolean isEmpty(){
            return this.getAddressBuilder().isEmpty() || this.getWayBuilder().isEmpty() || this.getRelationBuilder().isEmpty();
        }

        public long getId(){
            return this.id;
        }

        public int getIdasInt(){
            return (int) this.id;
        }
        public float getLat(){
            return this.lat;
        }
        public float getLon(){
            return this.lon;
        }
        
        public AddressBuilder getAddressBuilder(){
            return this.addressBuilder;
        }
        public WayBuilder getWayBuilder(){
            return this.wayBuilder;
        }
        public RelationBuilder getRelationBuilder(){
            return this.relationBuilder;
        }

        public String getName(){
            return name;
        }
        public Type getType(){
            return this.type;
        }
        public String getTypeValue(){
            return this.TypeValue;
        }

        /**
         * Parse the XML element and add the data to the builder(s).
         * @param element - The name of the element to parse.
         * @param reader - The {@link XMLStreamReader} to get the data from.
         */
        public void parse(String element, XMLStreamReader reader){
            switch (element) {
                case "node":
                    this.id = getAttributeByLong(reader, "id");
                    this.lat = MecatorProjection.projectLat(getAttributeByFloat(reader, "lat"));
                    this.lon = MecatorProjection.projectLon(getAttributeByFloat(reader, "lon"));
                    break;
                case "way":
                    this.id = getAttributeByLong(reader, "id");   
                    break;      
                case "relation":
                    this.id = getAttributeByLong(reader, "id");                  
                    break;
                case "tag":
                    String k = reader.getAttributeValue(null, "k");
                    String v = reader.getAttributeValue(null, "v");

                        parseTag(k, v);
                    break;
                case "nd":
                    TagNode node = XMLReader.getNodeById(getAttributeByLong(reader, "ref"));
                    wayBuilder.addNode(node);
                    break;
                case "member":
                    relationBuilder.parseMember(reader);
                    break;
                default:
                    break;
            }
        }

        /**
         * Parse a tag and add the data to the builder.
         * @param k - The key of the tag.
         * @param v - The value of the tag.
         */
        private void parseTag(String k, String v){
            if(k.equals("name")){
                this.name = v; // set the name of the node
            }

            if(k.contains("maxspeed")){
                try {
                    wayBuilder.setSpeedLimit(Integer.parseInt(v));
                    return;
                } catch (NumberFormatException e) {
                   return;
                }
            }

            if(k.equals("oneway")){
                wayBuilder.setOneWay(v.equals("yes") || v.equals("true"));
                return;
            }


            // check if the tag is a type tag and set the type
            for (Type currType : Type.getTypes()){
                if (k.equals(currType.getKey())){
                    for (String currVal : currType.getValue()) {
                        if (v.equals(currVal) || currVal.equals("")) {
                            
                            if(wayBuilder.getSpeedLimit() != 1){
                                for (Type roadType : Type.getAllRoads()) {
                                    if(currType.equals(roadType)){
                                        parseStreet(roadType);
                                    }
                                }
                            }
                            
                            switch (currType) { 
                                case ROUTE:
                                case RESTRICTION:
                                case MULTIPOLYGON:
                                    relationBuilder.setRelationType(currType);
                                    relationBuilder.setTypeValue(v);
                                    break;
                                default:
                                    this.type = currType; 
                                break;
                            } 
                            this.type = currType;
                            break;
                        }
                    }
                }
            }

            // if the tag is a address tag
            if(k.contains("addr:")){
                switch (k) {
                    case "addr:city":
                    addressBuilder.city(v);
                        break;
                    case "addr:country":
                    addressBuilder.country(v);
                        break;
                    case "addr:street":
                    addressBuilder.street(v);
                        break;
                    case "addr:housenumber":
                    addressBuilder.house(v);
                        break;
                    case "addr:postcode":
                    addressBuilder.postcode(v);
                        break;
                    case "addr:municipality":
                    addressBuilder.municipality(v);
                        break;
                    default:
                        break;
                }
            }
        }

        public void parseStreet(Type type){
            final int DEFAULT_SPEED = 50;

            switch (type) {
                case MOTORWAY:
                    wayBuilder.setSpeedLimit(130);
                    break;
                case PRIMARY_ROAD:
                case SECONDARY_ROAD:
                case TERTIARY_ROAD:
                    wayBuilder.setSpeedLimit(80);
                    break;
                case RESIDENTIAL_ROAD:
                    wayBuilder.setSpeedLimit(DEFAULT_SPEED);
                    break;
                case OTHER_ROAD:
                    wayBuilder.setSpeedLimit(DEFAULT_SPEED);
                    break;
                default:
                    wayBuilder.setSpeedLimit(DEFAULT_SPEED);
                    break;
            }
        }
    }
    





